FROM ubuntu:21.04

# ---------------------------------------------------------------------------------------------
# Install dependency packages
# ---------------------------------------------------------------------------------------------

ENV DEBIAN_FRONTEND=noninteractive

RUN apt update && \
    apt -y upgrade && \
    apt -y dist-upgrade && \
    apt install -y wget curl unzip python3-jsonschema openjdk-11-jdk && \
    apt autoclean -y && \
    apt remove -y && \
    apt autoremove -y && \
    apt clean -y

# ---------------------------------------------------------------------------------------------
# Set up android sdk
# ---------------------------------------------------------------------------------------------

ENV ANDROID_SDK_ROOT /opt/android-sdk
ENV ANDROID_SDK_HOME ${ANDROID_SDK_ROOT}
ENV ANDROID_HOME     ${ANDROID_SDK_ROOT}
ENV ANDROID_SDK      ${ANDROID_SDK_ROOT}
ENV ANDROID_NDK_HOME ${ANDROID_SDK_ROOT}/ndk
ENV ANDROID_NDK_ROOT ${ANDROID_SDK_ROOT}/ndk

RUN wget https://dl.google.com/android/repository/commandlinetools-linux-7302050_latest.zip -O ${ANDROID_SDK_ROOT}.zip && \
    unzip ${ANDROID_SDK_ROOT}.zip -d ${ANDROID_SDK_ROOT} && \
    rm -rf ${ANDROID_SDK_ROOT}.zip

ENV PATH ${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/bin:${ANDROID_SDK_ROOT}/tools/bin

RUN yes | sdkmanager --update --sdk_root=${ANDROID_SDK_ROOT} && \
    yes | sdkmanager "platforms;android-30" "platform-tools" "build-tools;30.0.2" --sdk_root=${ANDROID_SDK_ROOT}
