# Android Version plugin

Android Version plugin is a gradle plugin that helps managing version for Android projects. Plugin is especially helpful when you are dealing with complex build structure with multiple flavors and apk splits.

## Features

 - Version name and code manipulation
 - Per flavor & build type version manipulation
 - Per split version manipulation
 - Multi dimensional flavor version manipulation
 - Rule based automatic version code calculation
 - Rename apk file name

## Usage

### Apply plugin

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }

    dependencies {
         classpath "gradle.plugin.si.kamino.gradle:android-version:1.2.0"
    }
}

apply plugin: 'si.kamino.android-version'
```

### Configuration

```groovy
androidVersion {

    fileNamePattern 'sample-$flavors.dimension-$versionName'

    appVersion {
        major 1
        versionCode {
            digits 2
        }
    }

    variants {
        variant1 {
            minor 2
        }

        variant2 {
            minor 5
        }
        
        debug {
            build 1
        }
        
        variant1Debug {
            build 3
        }

    }
    
    splts {
        abi {
            x86 {
                versionCode(IncreaseVersionCode) {
                    by 1000000
                }
            }
        }
    }

}
```

#### Flavor combinations

Beside configuring version based on flavors and build types you can also do that on combinations of them when using 
multiple variant dimensions. Combinations are calculated based on dimension order. For example if you have `flavor1`
and `flavor2` that are in different dimensions you could do configuration for `flavor1Flavor2` and also 
`flavor1Flavor2Debug`. If you define both second one will override first one if they have sem properties set.

#### Version code types

Based on your need you can specify version code tactics that you want to use. By default 
`appVersion` uses `SimpleVersionCode` tactics but this can easily be changed. Version code 
tactics can also be changed per variant/split. It can ether modify existing version code
or specify completely  new value.

- `SimpleVersionCode` creates version code from version name parameters (`major`, `minor` and `build`). By default 
each part will take up 2 digits but this is configurable through `digits` parameter.
- `StaticVersionCode` sets static value defined in `versionCode` parameter.
- `IncreaseVersionCode` increases previously calculated value by value defined in `by` parameter.

By default plugin will set `SimpleVersionCode` with digits set to 2.

#### Output file name

With `fileNamePattern` property you can easily rename out `apk` files. Alongside static values you can specify template
and plugin will generate the file name. Available properties are:
 - `project`: name of the project. In case of default Android Studio template that would be `app`.
 - `variantName`: full unique name of the variant including filters.
 - `buildType`: build type of the variant
 - `flavorName`: name of all flavors combined with capitalized latter
 - `versionCode`: version code of output file including apk splits
 - `versionName`: version name of output file including apk splits
 - `flavors` by dimension map of all flavors. (for example: `flavors.dimension`)

`.apk` suffix is automatically added at the end of file name.

## Limitations 

There are some instant run limitations in versions before 2.3.0 of Android Gradle plugin due to [bug](https://code.google.com/p/android/issues/detail?id=227610).
Due to this issue plugin will be disabled if it detects that it is running under instant run. That means that version name will be empty 
and version code will be 0. That is ok for tho most cases, if not you can still set version the old way in `android.defaultConfig`.
When using Android Gradle Plugin 2.3.0 or newer everything works as espected.

## License 

    MIT License
    
    Copyright (c) 2017 Kamino d.o.o.
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
