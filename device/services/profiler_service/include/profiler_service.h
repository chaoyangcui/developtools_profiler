/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
#ifndef PROFILER_SERVICE_H
#define PROFILER_SERVICE_H

#include "logging.h"
#include "nocopyable.h"
#include "profiler_service.grpc.pb.h"
#include <memory>

class PluginService;

class PluginSession;
class ResultDemuxer;
class TraceFileWriter;
class ProfilerDataRepeater;

using PluginSessionPtr = STD_PTR(shared, PluginSession);
using PluginServicePtr = STD_PTR(shared, PluginService);

class ProfilerService : public IProfilerService::Service {
public:
    ProfilerService(const PluginServicePtr& pluginService = nullptr);

    ~ProfilerService();

    // get all plugin infos and capabilities.
    ::grpc::Status GetCapabilities(::grpc::ServerContext* context,
                                   const ::GetCapabilitiesRequest* request,
                                   ::GetCapabilitiesResponse* response) override;

    // create tracing sesion and pass tracing config to plugins.
    ::grpc::Status CreateSession(::grpc::ServerContext* context,
                                 const ::CreateSessionRequest* request,
                                 ::CreateSessionResponse* response) override;

    // start tracing session, active server side tracing triggers.
    ::grpc::Status StartSession(::grpc::ServerContext* context,
                                const ::StartSessionRequest* request,
                                ::StartSessionResponse* response) override;

    // get server-side cached tracing data since current session started.
    ::grpc::Status FetchData(::grpc::ServerContext* context,
                             const ::FetchDataRequest* request,
                             ::grpc::ServerWriter<::FetchDataResponse>* writer) override;

    // stop tracing session, deactive server side tracing triggers.
    ::grpc::Status StopSession(::grpc::ServerContext* context,
                               const ::StopSessionRequest* request,
                               ::StopSessionResponse* response) override;

    // destroy tracing session.
    ::grpc::Status DestroySession(::grpc::ServerContext* context,
                                  const ::DestroySessionRequest* request,
                                  ::DestroySessionResponse* response) override;

    bool StartService(const std::string& listenUri);

    void WaitServiceDone();

    void StopService();

private:
    static constexpr size_t DEFAULT_REPEATER_BUFFER_SIZE = 100;

    struct SessionContext {
        uint32_t id;
        std::mutex mutex;
        ProfilerSessionConfig sessionConfig;
        std::vector<ProfilerSessionConfig::BufferConfig> bufferConfigs;
        std::vector<ProfilerPluginConfig> pluginConfigs;
        std::vector<ProfilerPluginState> pluginStatus;
        std::map<std::string, PluginSessionPtr> pluginSessions;
        std::shared_ptr<ProfilerDataRepeater> dataRepeater;
        std::shared_ptr<TraceFileWriter> traceFileWriter;
        std::shared_ptr<ResultDemuxer> resultDemuxer;

        PluginSessionPtr CreatePluginSession(const PluginServicePtr& pluginService,
                                                           const ProfilerPluginConfig& pluginConfig);

        PluginSessionPtr CreatePluginSession(const PluginServicePtr& pluginService,
                                                           const ProfilerPluginConfig& pluginConfig,
                                                           const ProfilerSessionConfig::BufferConfig& bufferConfig);

        bool CheckBufferConfig(const ProfilerSessionConfig::BufferConfig& bufferConfig);

        bool CreatePluginSessions(const PluginServicePtr& pluginService);
        bool RemovePluginSessions(const std::vector<std::string>& nameList);
        bool UpdatePluginSessions(const PluginServicePtr& pluginService,
                                  const std::vector<int>& configIndexes);
        std::vector<int> UpdatePluginConfigs(const std::vector<ProfilerPluginConfig>& configList);

        SessionContext() = default;
        ~SessionContext();

        DISALLOW_COPY_AND_MOVE(SessionContext);
    };

    using SessionContextPtr = STD_PTR(shared, SessionContext);

    SessionContextPtr GetSessionContext(uint32_t sessionId) const;

    bool AddSessionContext(uint32_t sessionId, const SessionContextPtr& sessionCtx);

    bool RemoveSessionContext(uint32_t sessionId);

private:
    mutable std::mutex sessionContextMutex_;
    PluginServicePtr pluginService_;
    std::atomic<uint32_t> sessionIdCounter_ {0};
    std::atomic<uint32_t> responseIdCounter_ {0};
    std::map<uint32_t, SessionContextPtr> sessionContext_;
    std::unique_ptr<grpc_impl::Server> server_;

    DISALLOW_COPY_AND_MOVE(ProfilerService);
};

#endif // PROFILER_SERVICE_H
