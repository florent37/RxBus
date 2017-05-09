package florent37.github.com.rxsharedpreferences.bus;

import com.github.florent37.rxsharedpreferences.RxBus;

import florent37.github.com.rxsharedpreferences.model.User;
import io.reactivex.Observable;

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
