#
# Copyright (C) 2020 Project 404
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Include mokee common configuration
TARGET_BOOT_ANIMATION_RES := 1080

TARGET_GAPPS_ARCH := arm64

# Inherit device configuration
$(call inherit-product, device/xiaomi/cmi/kona.mk)

# Inherit common mokee configuration
$(call inherit-product, vendor/mokee/config/common_full_phone.mk)

# $(call inherit-product, vendor/google/pixel/config.mk)
# $(call inherit-product, vendor/google/gms/gapps.mk)

# Override build properties
PRODUCT_NAME := mokee_cmi
PRODUCT_DEVICE := cmi
PRODUCT_BRAND := Xiaomi
PRODUCT_MODEL := Mi 10 Pro
PRODUCT_MANUFACTURER := Xiaomi

BUILD_FINGERPRINT := "Xiaomi/cmi/cmi:11/RKQ1.200826.002/V12.2.2.0.RJACNXM:user/release-keys"

PRODUCT_GMS_CLIENTID_BASE := android-xiaomi
