#include <jni.h>
#include <string>

//extern 声明在bspatch.c
extern "C"{
    extern int p_main(int argc,char * argv[]);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_bing_mybsdiff_MainActivity_bsPatch(JNIEnv *env, jobject instance, jstring oldApk_,
                                            jstring patch_, jstring output_) {
    //将java字符串转为C/C++的字符串，也可以理解为转化为UTF-8格式的char指针
    const char *oldApk = env->GetStringUTFChars(oldApk_, 0);
    const char *patch = env->GetStringUTFChars(patch_, 0);
    const char *output = env->GetStringUTFChars(output_, 0);

    //bspatch oldFile newFile patch
    char * argv[] = {"", const_cast<char *>(oldApk), const_cast<char *>(output),
                     const_cast<char *>(patch)};
    p_main(4,argv);

    //释放指向Unicode格式的char指针
    env->ReleaseStringUTFChars(oldApk_, oldApk);
    env->ReleaseStringUTFChars(patch_, patch);
    env->ReleaseStringUTFChars(output_, output);
}