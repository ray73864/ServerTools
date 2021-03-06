ServerTools
=========
**This repository is for ServerTools 2.0 and beyond (Still in development!)**

Servertools is a server-side minecraft mod that adds a variety of utilities and commands as well as a simple permission system.

[Minecraft Forums Thread] (http://www.minecraftforum.net/topic/1835347-)

[Curse Forge Page] (http://www.curse.com/mc-mods/minecraft/forgeservertools)

[License] (#license)

##Depending on ServerTools
```groovy
repositories {
    mavenCentral()
    maven {
        name 'Matthews Maven Repo'
        url 'http://maven.matthewprenger.com'
    }
}

dependencies {
    compile 'com.matthewprenger.servertools:ServerTools-CORE:1.7.2-2.0.+'
}
```

Just add the above code to your build.gradle, refresh your project, and you will have access to ServerTools code.
If you want to depend on multiple modules, refer to the following code block

```groovy
dependencies {
    compile 'com.matthewprenger.servertools:ServerTools-CORE:1.7.2-2.0.+'
    compile 'com.matthewprenger.servertools:ServerTools-BACKUP:1.7.2-2.0.+'
}
```

##License
Copyright &copy; 2014 matthewprenger

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this program except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
