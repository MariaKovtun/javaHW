
public class Main {

    public static void main(String[] args)  throws Exception {
        ru.netology.graphics.image.TextGraphicsConverter converter = new MyConverter(); // Создайте тут объект вашего класса конвертера
        ru.netology.graphics.server.GServer server = new ru.netology.graphics.server.GServer(converter); // Создаём объект сервера
        server.start(); // Запускаем

            // Или то же, но с сохранением в файл:
        /*
        PrintWriter fileWriter = new PrintWriter(new File("converted-image.txt"));
        converter.setMaxWidth(200);
        converter.setMaxHeight(300);
        fileWriter.write(converter.convert("https://i.ibb.co/6DYM05G/edu0.jpg"));
        fileWriter.close();
        */
    }
}
