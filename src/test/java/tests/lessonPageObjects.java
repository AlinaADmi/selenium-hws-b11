package tests;

import app.Application;
import org.junit.Before;
import org.junit.Test;

public class lessonPageObjects {
    public static ThreadLocal<Application> AppThread = new ThreadLocal<>();
    public Application app;

    @Before
    public void start() {
        if (AppThread.get() != null) {
            app = AppThread.get();
            return;
        }

        app = new Application();
        AppThread.set(app);

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    app.quit();
                    app = null;
                }));
    }

    @Test
    public void addRemoveProductFromCart() {
        app.mainPage.open();

        for (int i = 1; i <= 3; i++) {
            app.mainPage.chooseFirstItem();
            app.productPage.addToCart();
        }
        app.mainPage.open();
        app.cartPage.removeFromCart();
    }
}
