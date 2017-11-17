# RxBus

Android reactive event bus that simplifies communication between Presenters, Activities, Fragments, Threads, Services, etc. 

<a target='_blank' rel='nofollow' href='https://app.codesponsor.io/link/iqkQGAc2EFNdScAzpwZr1Sdy/florent37/RxBus'>
  <img alt='Sponsor' width='888' height='68' src='https://app.codesponsor.io/embed/iqkQGAc2EFNdScAzpwZr1Sdy/florent37/RxBus.svg' />
</a>

# Download

<a href='https://ko-fi.com/A160LCC' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=0' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

[ ![Download](https://api.bintray.com/packages/florent37/maven/rxbus/images/download.svg) ](https://bintray.com/florent37/maven/rxbus/_latestVersion)
```java
dependencies {
    compile 'com.github.florent37:rxbus:1.0.0'
}
```

# RxBus in 2 steps

1. Prepare subscribers 

```java
RxBus.getDefault()
                .onEvent("eventName")
                .doOnSubscribe(disposables::add)
                .subscribe(s -> {
                     //called when an event "eventName" is posted      
                });
```

2. Post event

```java
RxBus.getDefault().post("eventName");
```

# Messages types

```java
rxbus.post(String) <-> rxbus.onEvent(String)

rxbus.post(userInstance) <-> rxbus.onEvent(User.class)
```

# Extended Bus

1. Extend RxBus for a specific usage 

```
public class RxBusUser extends RxBus {

    private static RxBusUser instance = new RxBusUser();

    public static RxBusUser getInstance() {
        return instance;
    }

    public Observable<User> onUserChanged(){
        return super.onEvent(User.class);
    }

    public void userChanged(User user){
        super.post(user);
    }

    public void displayUser(boolean display){
        super.post(new DisplayUserCustomEvent(display));
    }

    public Observable<Boolean> onDisplayUser(){
        return super.onEvent(DisplayUserCustomEvent.class)
                .map(DisplayUserCustomEvent::displayUser);
    }

    protected class DisplayUserCustomEvent {

        public boolean displayUser;

        public DisplayUserCustomEvent(boolean displayUser) {
            this.displayUser = displayUser;
        }

        public boolean displayUser() {
            return displayUser;
        }
    }

}
```

2. Register to user change

```java
final RxBusUser rxBusUser = RxBusUser.getInstance();

rxBusUser.onUserChanged()
        .doOnSubscribe(disposables::add)
        .subscribe(user -> display(user));
```

3. Post the user
 
```java
RxBusUser.getInstance().userChanged(user);
```

# Unsubscribe

1. don't forget to add the disposable to a CompositeDisposable `.doOnSubscribe(compositeDisposable::add)`

2. OnDestroy should call `compositeDisposable.clear()` 

# Credits

Author: Florent Champigny [http://www.florentchampigny.com/](http://www.florentchampigny.com/)

Blog : [http://www.tutos-android-france.com/](http://www.www.tutos-android-france.com/)

<a href="https://play.google.com/store/apps/details?id=com.github.florent37.florent.champigny">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>
<a href="https://plus.google.com/+florentchampigny">
  <img alt="Follow me on Google+"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/gplus.png" />
</a>
<a href="https://twitter.com/florent_champ">
  <img alt="Follow me on Twitter"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/twitter.png" />
</a>
<a href="https://www.linkedin.com/in/florentchampigny">
  <img alt="Follow me on LinkedIn"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/linkedin.png" />
</a>


License
--------

    Copyright 2017 Florent37, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
