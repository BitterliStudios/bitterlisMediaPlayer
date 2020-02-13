/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_synthbot_audioplugin_vst_vst2_JVstHost20 */

#ifndef _Included_com_synthbot_audioplugin_vst_vst2_JVstHost20
#define _Included_com_synthbot_audioplugin_vst_vst2_JVstHost20
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    setThis
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_setThis
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    processReplacing
 * Signature: ([Ljavax/sound/midi/MidiMessage;[[F[[FIJ)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_processReplacing
  (JNIEnv *, jclass, jobjectArray, jobjectArray, jobjectArray, jint, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    canReplacing
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_canReplacing
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    process
 * Signature: ([Ljavax/sound/midi/MidiMessage;[[F[[FIJ)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_process
  (JNIEnv *, jclass, jobjectArray, jobjectArray, jobjectArray, jint, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    canDo
 * Signature: (Ljava/lang/String;J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_canDo
  (JNIEnv *, jclass, jstring, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    setParameter
 * Signature: (IFJ)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_setParameter
  (JNIEnv *, jclass, jint, jfloat, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getParameter
 * Signature: (IJ)F
 */
JNIEXPORT jfloat JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getParameter
  (JNIEnv *, jclass, jint, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    isParameterAutomatable
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_isParameterAutomatable
  (JNIEnv *, jclass, jint, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getParameterName
 * Signature: (IJ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getParameterName
  (JNIEnv *, jclass, jint, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getParameterDisplay
 * Signature: (IJ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getParameterDisplay
  (JNIEnv *, jclass, jint, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getParameterLabel
 * Signature: (IJ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getParameterLabel
  (JNIEnv *, jclass, jint, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getEffectName
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getEffectName
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getVendorName
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getVendorName
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getProductString
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getProductString
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    numParameters
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_numParameters
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    numInputs
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_numInputs
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    numOutputs
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_numOutputs
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    numPrograms
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_numPrograms
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    setSampleRate
 * Signature: (FJ)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_setSampleRate
  (JNIEnv *, jclass, jfloat, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    setTempo
 * Signature: (DJ)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_setTempo
  (JNIEnv *, jclass, jdouble, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    setBlockSize
 * Signature: (IJ)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_setBlockSize
  (JNIEnv *, jclass, jint, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getUniqueId
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getUniqueId
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    isSynth
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_isSynth
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    acceptsProgramsAsChunks
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_acceptsProgramsAsChunks
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    openEditor
 * Signature: (Ljava/lang/String;J)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_openEditor
  (JNIEnv *, jclass, jstring, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    topEditor
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_topEditor
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    closeEditor
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_closeEditor
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    hasEditor
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_hasEditor
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getProgram
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getProgram
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    setProgram
 * Signature: (IJ)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_setProgram
  (JNIEnv *, jclass, jint, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getProgramName
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getProgramName__J
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getProgramName
 * Signature: (IJ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getProgramName__IJ
  (JNIEnv *, jclass, jint, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    setProgramName
 * Signature: (Ljava/lang/String;J)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_setProgramName
  (JNIEnv *, jclass, jstring, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getPluginVersion
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getPluginVersion
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getInitialDelay
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getInitialDelay
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    resume
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_resume
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    suspend
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_suspend
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    setChunk
 * Signature: (I[BJ)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_setChunk
  (JNIEnv *, jclass, jint, jbyteArray, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getChunk
 * Signature: (IJ)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getChunk
  (JNIEnv *, jclass, jint, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    setBypass
 * Signature: (ZJ)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_setBypass
  (JNIEnv *, jclass, jboolean, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    getPinProperties
 * Signature: (IZJ)Lcom/synthbot/audioplugin/vst/vst2/VstPinProperties;
 */
JNIEXPORT jobject JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_getPinProperties
  (JNIEnv *, jclass, jint, jboolean, jlong);

/*
 * Class:     com_synthbot_audioplugin_vst_vst2_JVstHost20
 * Method:    setTimeSignature
 * Signature: (IIJ)V
 */
JNIEXPORT void JNICALL Java_com_synthbot_audioplugin_vst_vst2_JVstHost20_setTimeSignature
  (JNIEnv *, jclass, jint, jint, jlong);

#ifdef __cplusplus
}
#endif
#endif
