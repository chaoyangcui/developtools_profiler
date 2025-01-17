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
#ifndef TRANCE_FILE_WRITER_H
#define TRANCE_FILE_WRITER_H

#include "logging.h"
#include "nocopyable.h"
#include "writer.h"

#include <fstream>
#include <google/protobuf/message_lite.h>
#include <string>

using google::protobuf::MessageLite;

class TraceFileWriter : public Writer {
public:
    explicit TraceFileWriter(const std::string& path);

    ~TraceFileWriter();

    bool Open(const std::string& path);

    long Write(const MessageLite& message);

    long Write(const void* data, size_t size) override;

    bool Flush() override;

private:
    std::string path_;
    std::ofstream stream_;

    DISALLOW_COPY_AND_MOVE(TraceFileWriter);
};

using TraceFileWriterPtr = STD_PTR(shared, TraceFileWriter);

#endif // !TRANCE_FILER_WRITER_H