package florent37.github.com.rxsharedpreferences;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.florent37.rxsharedpreferences.RxBus;

import florent37.github.com.rxsharedpreferences.bus.RxBusUser;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disposables = new CompositeDisposable();

        registerToEvents();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, MainFragment.newInstance())
                .commitAllowingStateLoss();
    }

    private void registerToEvents() {
        final RxBusUser rxBusUser = RxBusUser.getInstance();

        rxBusUser.onUserChanged()
                .doOnSubscribe(disposables::add)
                .subscribe(
                        this::display,
                        this::displayError
                );

        rxBusUser.onEvent("myMessage")
                .doOnSubscribe(disposables::add)
                .subscribe(
                        this::display,
                        this::displayError
                );

        rxBusUser.allEvents()
                .doOnSubscribe(disposables::add)
                .subscribe(
                        this::display,
                        this::displayError
                );

        RxBus.getDefault()
                .onEvent("eventName")
                .doOnSubscribe(disposables::add)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        this.disposables.clear();
        super.onDestroy();
    }

    private void display(Object o) {
        Toast.makeText(getBaseContext(), o.toString(), Toast.LENGTH_SHORT).show();
    }

    private void displayError(Throwable t) {
        Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
    }
}
