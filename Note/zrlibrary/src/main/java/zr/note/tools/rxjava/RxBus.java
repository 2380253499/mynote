package zr.note.tools.rxjava;


import com.trello.rxlifecycle.LifecycleProvider;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 使用RxJava代替EventBus的方案
 *
 * @author thanatos
 * @create 2016-01-05
 **/
public class RxBus {

    private static RxBus rxBus;
    private final Subject<Events<?>, Events<?>> _bus = new SerializedSubject<>(PublishSubject.<Events<?>>create());

    private RxBus() {
    }

    public static RxBus getInstance() {
        if (rxBus == null) {
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void send(Events<?> o) {
        _bus.onNext(o);
    }

//    public void send(@Events.EventCode int code, Object content) {
    public void send( int code, Object content) {
        Events<Object> event = new Events<>();
        event.code = code;
        event.content = content;
        send(event);
    }

    public Observable<Events<?>> toObservable() {
        return _bus;
    }

    public static SubscriberBuilder with(LifecycleProvider provider) {
        return new SubscriberBuilder(provider);
    }

    public static class SubscriberBuilder {
        private LifecycleProvider lifecycleProvider;
        private int event;
        private Action1<? super Events<?>> onNext;
        private Action1<Throwable> onError;

        public SubscriberBuilder(LifecycleProvider provider) {
            this.lifecycleProvider = provider;
        }

//        public SubscriberBuilder setEvent(@Events.EventCode int event) {
        public SubscriberBuilder setEvent( int event) {
            this.event = event;
            return this;
        }


        public SubscriberBuilder onNext(Action1<? super Events<?>> action) {
            this.onNext = action;
            return this;
        }

        public SubscriberBuilder onError(Action1<Throwable> action) {
            this.onError = action;
            return this;
        }


        public void create(Scheduler io, Scheduler mainThread) {
            _create(io, mainThread);
        }

        public void create() {
            _create(AndroidSchedulers.mainThread(), AndroidSchedulers.mainThread());
        }

        public Subscription _create(Scheduler io, Scheduler mainThread) {
            if (lifecycleProvider != null) {
                return RxBus.getInstance().toObservable()
                        .compose(lifecycleProvider.<Events<?>>bindToLifecycle()) // 绑定生命周期
                        .filter(new Func1<Events<?>, Boolean>() {
                            @Override
                            public Boolean call(Events<?> events) {
                                return events.code == event;
                            }
                        })   //过滤 根据code判断返回事件
                        .subscribeOn(io).observeOn(mainThread)
                        .subscribe(onNext, onError == null ? new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } : onError);
            }
            return null;
        }
    }
}
