LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := ndkbookingservice
LOCAL_SRC_FILES := ndkbookingservice.c

include $(BUILD_SHARED_LIBRARY)