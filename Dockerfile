FROM adoptopenjdk/openjdk8:alpine

WORKDIR /

SHELL ["/bin/sh", "-c"]

ARG DEBIAN_FRONTEND=noninteractive

# Install bash tools.
RUN apk update && \
    apk upgrade && \
    apk add --no-cache \
    bash \
    git \
    curl \
    wget \
    zip \
    unzip \
    ruby \
    ruby-rdoc \
    ruby-irb \
    ruby-dev \
    ruby-webrick \
    openssh \
    g++ \
    make \
    libvirt-daemon \
    qemu-img \
    qemu-system-x86_64 \
    dbus \
    polkit \
    virt-manager \
    && rm -rf /tmp/* /var/tmp/*

RUN apk --no-cache add ca-certificates wget

# android pre-installed sdk tools/libs
ARG ANDROID_COMPILE_SDK_VERSION=33
ARG ANDROID_BUILD_TOOL_VERSION="33.0.1"
ARG ANDROID_VERSION="android-${ANDROID_COMPILE_SDK_VERSION}"
ARG ANDROID_EMULATOR_PACKAGE_x86_64="system-images;${ANDROID_VERSION};google_apis;x86_64"
ARG ANDROID_PLATFORM_VERSION="platforms;${ANDROID_VERSION}"
ARG ANDROID_SDK_VERSION="sdk-tools-linux-4333796.zip"
ARG ANDROID_SDK_PACKAGES_EXTRA="build-tools;${ANDROID_BUILD_TOOL_VERSION}"
ARG ANDROID_SDK_PACKAGES="${ANDROID_EMULATOR_PACKAGE_x86_64} ${ANDROID_PLATFORM_VERSION} platform-tools ${ANDROID_SDK_PACKAGES_EXTRA}"

RUN wget https://dl.google.com/android/repository/${ANDROID_SDK_VERSION} -P /tmp && \
    unzip -d /opt/android /tmp/${ANDROID_SDK_VERSION}
ENV ANDROID_HOME=/opt/android
ENV PATH "$PATH:$ANDROID_HOME/emulator:$ANDROID_HOME/tools/bin:$ANDROID_HOME/platform-tools"

# sdkmanager
RUN mkdir /root/.android/
RUN touch /root/.android/repositories.cfg
RUN yes Y | sdkmanager --licenses
RUN yes Y | sdkmanager --verbose --no_https ${ANDROID_SDK_PACKAGES}

# avdmanager
ENV EMULATOR_NAME_x86="android_x86"
RUN echo "no" | avdmanager --verbose create avd --force --name "${EMULATOR_NAME_x86}" --device "pixel" --package "${ANDROID_EMULATOR_PACKAGE_x86_64}"
ENV LD_LIBRARY_PATH "$ANDROID_HOME/emulator/lib64:$ANDROID_HOME/emulator/lib64/qt/lib"

# Install Fastlane
RUN wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub
RUN wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.26-r0/glibc-2.26-r0.apk
RUN wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.26-r0/glibc-bin-2.26-r0.apk
RUN wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.26-r0/glibc-i18n-2.26-r0.apk

RUN apk add glibc-2.26-r0.apk
RUN apk add glibc-bin-2.26-r0.apk
RUN apk add glibc-i18n-2.26-r0.apk
RUN /usr/glibc-compat/bin/localedef -i en_US -f UTF-8 en_US.UTF-8

RUN gem install fastlane -v 2.211.0

##firebase-tools setup
ADD https://github.com/firebase/firebase-tools/releases/download/v11.22.0/firebase-tools-linux firebase-tools-linux
RUN chmod +x firebase-tools-linux
# if we use firebase tools <7.4.0 we need additional command firebase --open-sesame appdistribution because until now this feature was in closed alpha.
# RUN ./firebase-tools-linux --open-sesame appdistribution

# Install gradle
WORKDIR /gradle/wrapper/dists

ENV GRADLE_VERSION 7.4
RUN wget  https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-all.zip && \
    unzip gradle-$GRADLE_VERSION-all.zip && \
#    rm -f gradle-$GRADLE_VERSION-all.zip && \
    mv gradle-$GRADLE_VERSION gradle-$GRADLE_VERSION-all && \
    ln -s gradle-$GRADLE_VERSION-all $GRADLE_HOME && \
    echo -ne "- with Gradle $GRADLE_VERSION\n" >> /root/.built

ENV GRADLE_HOME /gradle/wrapper/dists/gradle-$GRADLE_VERSION-all
ENV GRADLE_USER_HOME /gradle
ENV PATH ${PATH}:${GRADLE_HOME}/bin

WORKDIR /

RUN apk update && apk add libstdc++ && rm -rf /var/cache/apk/*

## Remove comment for local testing.
COPY . /GenericAdapter

# clean up
RUN apk del unzip wget && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* /var/cache/apk/*

# ADD start.sh /
# RUN chmod +x start.sh
