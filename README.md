# android-koltin-study
android-kotlin学习

下载 android-studio3-Canary2
配置gradle为 android-studio3-Canary2\gradle\gradle-4.0-milestone-1
gradle -v查看
下载gradle-4.0-milestone-1-all.zip 。，放到 C:\Users\XXX\.gradle\wrapper\dists\gradle-4.0-milestone-1-all\2rnr7rhi2zsmkxo9re7615fy6

设置离线gradle：
File->Setting->Gradle页: 选中 Use local gradle distribution 选项，Gradle home 选择gradle的home目录

kotlin官网： https://kotlinlang.org/
kotlin to android developers: 
http://www.ctolib.com/docs/sfile/kotlin-for-android-developers-zh/ce_shi_shi_fou_yi_qie_jiu_xu.html

https://wangjiegulu.gitbooks.io/kotlin-for-android-developers-zh/content/zen_yaoqu_shi_yong_kotlinandroid_extensions.html

build.gradle中增加: 
module的build.gradle中： apply plugin: 'kotlin-android-extensions'
compile "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"

project的build.gradle中增加： 
classpath "org.jetbrains.kotlin:kotlin-android-extensions:$kotlin_version"

出现unresolved: kotlinx.coroutines.experimental.async
引入anko包：
org.jetbrains.anko:anko-coroutines   (编译各种不通过)


alt+enter 导入jar包

执行外部请求， AndroidManifest.xml中添加
<uses-permission android:name="android.permission.INTERNET" />

HTTP请求不被允许在主线程中执行，否则它会抛出异常
异步调用：
import kotlinx.coroutines.experimental.async
从 https://dl.bintray.com/jetbrains/anko/org/jetbrains/anko/anko-coroutines/ 下载jar包，放到本地libs目录
依赖 stdlib， 及org.jetbrains.kotlinx:kotlinx-coroutines-android:0.15
kotlinx-coroutines-android 依赖 org.jetbrains.kotlinx:kotlinx-coroutines-core:0.15
http://repo.gradle.org/gradle/repo/org/jetbrains/kotlinx/

maven central仓库下载gradle aar文件：
http://mvnrepository.com/
 http://central.maven.org/maven2
 https://jcenter.bintray.com
 如果只是一个简单的类库那么使用生成的*.jar文件即可；如果你的是一个UI库，包含一些自己写的控件布局文件以及字体等资源文件那么就只能使用*.aar文件

 将下载下来的jar放入工程的libs目录下， 
 build.gradle中 dependencies中包含： compile fileTree(dir: 'libs', include: ['*.jar'])

 添加aar文件： 如test.aar文件：
 首先拷贝到libs目录下， build.gradle中加入：
 repositories {  
    flatDir {  
        dirs 'libs'  
    }  
}  
dependencies {  
    compile(name:'test', ext:'aar')  
}  

主线程访问网络，debug报 android.os.NetworkOnMainThreadException

async(UI)方法提示：
Required:
CoroutineContext
Found:
HandlerContext     
kotlinx-coroutines-android-0.15.jar包里HandleCOntext.UI    

--------------------------
 gradle本地缓存目录：
     gradle\gradle-4.0-milestone-1\caches\modules-2 下面能看到本地缓存的jar包

访问网络需要添加 AndroidManifest.xml
<uses-permission android:name="android.permission.INTERNET" /> 

除了apply-plugin
classpath "org.jetbrains.kotlin:kotlin-android-extensions:$kotlin_version"


------------------

Kotlin: For-loop must have an iterator method - is this a bug?


settings-plugins install jetbrains plugin,  
 install anko-support   advanced java floding 

 代码自动检测错误，但编译正常。如split， readText
 按alt+enter， 提示创建 CharSequence.split的方法，这个方法在kotlin-stdlib-1.1.2-4里。将这个包放工程的libs下面， 就没问题，能自动提示。
重启后，编译会提示  Error:Error converting bytecode to dex: Cause: com.android.dex.DexException:   包重复
将  implementation fileTree(dir: 'libs', include: ['*.jar']) 改为
provided fileTree(dir: 'libs', include: ['*.jar'])


 kotlin-java混合：
 https://github.com/JetBrains/kotlin-examples/tree/master/gradle/android-mixed-java-kotlin-project

 --------------------------------
 布局相关：
 FrameLayout： 新建helloword工程默认布局
 RelativeLayout： 相对布局。
 LinearLayout + RelativeLayout：
 TableLayout：元素以行或列形式进行， 配合 TableRow里面，加入TextView EditText

 LinearLayout：线性布局， 该标签下的所有子元素根据orientation属性(verticale，horizontal)决定是按行还是列逐个显示。 一行或一列只能显示一个组件

 RelativeLayout: 常用是线性和相对布局的嵌套。


 -----------------
 OCR识别：
 参考：https://github.com/yushulx/android-tesseract-ocr
 libs下加入jar包及so包
 修改AndroidManifest.xml

1 启动时报错： java.lang.RuntimeException: Unable to start activity ComponentInfo
setContentView 设置的视图不对
报类找不到 TessBaseApPI找不到，
SDK TOOLS里勾选安装NDK

tess-tow版本：https://github.com/rmtheis/tess-two 直接加入gradle依赖下载包就好了

加入语言包：
右击app文件夹，选择New->Folder->Assets Folder， 右击新建的assets文件夹，选择New->Directory，在弹出的对话框中，输入新建文件夹名称为tessdata！ 一定要是tessdata， 
右击新建的tessdata文件夹，选项Show in Explorer，点击进入tessdata文件夹内
放入eng.traineddata 从github下载

照片需要权限。

模拟器打开 enable camera， 分配sd卡空间

https://jcenter.bintray.com 下载

启动报错：
java.lang.IllegalArgumentException: Data path does not exist!

无法创建目录。 未加载sd卡， AVD manager里， advanced，sdcard配置选择external
外部files
创建sd卡,进入adnroid/sdk/tools mksdcard 100M F:/sdcard.iso

进入 F:\android\SDK\platform-tools， 命令行 adb shell， 可以查看文件系统，将sdcard权限改为777
Android Device Monitor可以托文件进去
图片放sd根目录
重启上述修改呗清楚

mount sdcard using adb:

adb devices 查看device
