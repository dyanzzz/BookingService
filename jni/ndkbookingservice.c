#include<jni.h>
#include<string.h>
#include <stdio.h>

jstring Java_com_hyundaimobil_bookingservice_app_Config_server(JNIEnv* env, jobject obj){
    //return (*env)->NewStringUTF(env, "https://www.hyvision-hyundaimobil.com/ihyundai/index.php/android/");
    //return (*env)->NewStringUTF(env, "http://115.85.64.151/ihyundai/index.php/android/");
    //return (*env)->NewStringUTF(env, "https://www.hyvision-hyundaimobil.com/ihyundai-apps/index.php/android/");
    //return (*env)->NewStringUTF(env, "http://115.85.64.149/ihyundai/index.php/android/");
    //return (*env)->NewStringUTF(env, "http://192.168.1.244/ihyundai/index.php/android/");
    return (*env)->NewStringUTF(env, "http://apphyvision.hyvision-hyundai.com/ihyundai/index.php/android/");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_usernameHttps(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "slshyundai");
    //return (*env)->NewStringUTF(env, "hyundaiindonesia");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_passwordHttps(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "avega");
    //return (*env)->NewStringUTF(env, "hyundai2030");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_login(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "auth/login");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_logout(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "auth/logout");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_changePassword(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "change_pass/changepassword");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_chekcPaswd(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "change_pass/chekcPaswd");
}



jstring Java_com_hyundaimobil_bookingservice_app_Config_searchDealerSvc(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/search_dealer_svc");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_searchDealer(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/search_dealer");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_addBookingNow(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/add_booking_now");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getAllPromotions(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_all_promotions");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getSelectedPromotions(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_selected_promotions");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getAccount(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_account");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getAllBooking(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_all_booking");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getBookingDetail(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_booking_detail");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_searchDealerDetail(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/search_dealer_detail");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_cancelBooking(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/cancel_booking");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getAllNews(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_all_news");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getDetailNews(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_detail_news");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getServiceHistory(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_service_history");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getServiceHistoryDetail(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_service_history_detail");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_addRegisterTestDrive(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/add_register_test_drive");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_sendMailEmergency(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/send_mail_emergency");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getAllTipeKendaraan(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_all_tipe_kendaraan");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_updateApps(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/update_apps");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_registerAccount(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/register_account");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getAllMaintenanceService(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_all_maintenance_service");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getChasisRegister(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_chasis_register");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_pilihUnit(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/pilih_unit");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_unitDetail(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/unit_detail");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_forgotPassword(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/forgot_password");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getDateLibur(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_date_libur");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getAllInbox(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_all_inbox");
}

jstring Java_com_hyundaimobil_bookingservice_app_Config_getInboxDetail(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env, "booking_service/get_inbox_detail");
}

