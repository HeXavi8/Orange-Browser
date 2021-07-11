### English | [中文](./Chinese_Introduction)
# <img src='./images/title.png' height='60'/>

Orange Browser is a android application which developed during a six-week training program jointly held by ByteDance and South China University of Technology(SCUT).

<img src='./images/unity.png' height='60'/>


## Table of Contents
* [Snapshots](#Snapshots)
* [Functions](#Functions)
* [Usage](#Usage)
* [Documentations](#Documentations)
* [Design](#Design)
* [Project Members](#Project_Members)
* [Gifs](#Gifs)
* [License](#License)


## Snapshots <a name="Snapshots"></a>

<img src='./images/home_light.png' width='200'/>&emsp;<img src='./images/home_dark.png' width='200'/>

## Functions <a name="Functions"></a>

* Bookmarks
* History
* Quick page
* User management (Register & Login & Information Change)
* Incognito mode
* Dark mode
* Multimedia (Video Player & Photo Viewer & Photo Download)
* Search

## Usage <a name="Usage"></a>

Please clone the repository and run in Android Studio.

Gradle version:
```
distributionUrl=https\://services.gradle.org/distributions/gradle-6.7.1-all.zip
```
Dependencies:
```
dependencies {
    implementation 'com.github.bumptech.glide:glide:4.8.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.8.0'
    implementation fileTree(dir: "libs", include: ["*.jar"])
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.android.support:appcompat-v7:21.0.3'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'com.github.chrisbanes:PhotoView:2.0.0'
    implementation 'org.greenrobot:greendao:3.3.0'
    implementation 'androidx.recyclerview:recyclerview:1.1.0'
    implementation 'org.jetbrains:annotations:15.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    }
```

## Documentations <a name="Documentations"></a>
* [Requirements Research and Analysis](https://gmja06lqlv.feishu.cn/docs/doccnHKdKTbgC3bJ3vR0YWX3pdh#)
* [Interaction Design Documentation](https://gmja06lqlv.feishu.cn/docs/doccnL7AnOJU59VLdlAdy6n4DXc#)
* [Technical Design Documentation](https://gmja06lqlv.feishu.cn/docs/doccnA3Ya4Bk4qzcJY2D772wJcb#)

## Design <a name="Design"></a>

<img src='./images/logo_text.png' height='60'/>

Our logo and user interface were designed by [Zixuan](https://github.com/coddlly) and [Xavi](https://github.com/HeXavi8).

## Project Members <a name="Project_Members"></a>

- [Xavi](https://github.com/HeXavi8) - **Xavi He** &lt; 825308876@qq.com&gt; (he/him)
- [Li Xiaofei](https://github.com/Makka-Pakka111) - **Li Xiaofei** &lt;1040314319@qq.com&gt; (she/her)
- [Walden](https://github.com/Aoliao-w) - **Walden** &lt;1215454179@qq.com&gt; (she/her)
- [Zixuan](https://github.com/coddlly) - **Zixuan** &lt;1906377395@qq.com&gt; (she/her)

Our code and design are far from perfect. If you have any suggestions or would like to contribute code, please feel free to contact us or make pull requests. </br>

## Gifs
<img src='./images/splash.gif' width='160'/>&emsp;<img src='./images/search.gif' width='150'/>&emsp;<img src='./images/history_bookmark.gif' width='150'/>&emsp;<img src='./images/quick_page.gif' width='150'/>&emsp;<img src='./images/dark_mode.gif' width='150'/>

## License <a name="License"></a>
[Apache-2.0 License](./LICENSE)
