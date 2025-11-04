import java.time.LocalDateTime;

interface Message {
    String getContent();
}

class SimpleMessage implements Message {
    private String text;

    public SimpleMessage(String text) {
        this.text = text;
    }

    @Override
    public String getContent() {
        return this.text;
    }
}

abstract class MessageDecorator implements Message {
    protected Message wrappedMessage;

    public MessageDecorator(Message message) {
        this.wrappedMessage = message;
    }

    @Override
    public String getContent() {
        return wrappedMessage.getContent();
    }
}

class EncryptDecorator extends MessageDecorator {
    public EncryptDecorator(Message message) {
        super(message);
    }

    @Override
    public String getContent() {
        String original = super.getContent();
        return encrypt(original);
    }

    private String encrypt(String text) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                encrypted.append((char) (c + 1));
            } else {
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }
}

class CompressDecorator extends MessageDecorator {
    public CompressDecorator(Message message) {
        super(message);
    }

    @Override
    public String getContent() {
        String original = super.getContent();
        return original.replaceAll("\\s+", " ").trim();
    }
}

class TimestampDecorator extends MessageDecorator {
    public TimestampDecorator(Message message) {
        super(message);
    }

    @Override
    public String getContent() {
        String original = super.getContent();
        String timestamp = "[" + LocalDateTime.now().withNano(0) + "]";
        return timestamp + " " + original;
    }
}

class AuthorDecorator extends MessageDecorator {
    private String authorName;

    public AuthorDecorator(Message message, String authorName) {
        super(message);
        this.authorName = authorName;
    }

    @Override
    public String getContent() {
        String original = super.getContent();
        return original + " [Автор: " + this.authorName + "]";
    }
}

public class ChatDemo {
    public static void main(String[] args) {
        Message myMessage = new SimpleMessage("  Привіт,   це    базове   повідомлення. ");
        System.out.println("1. Базове повідомлення:\n'" + myMessage.getContent() + "'");
        System.out.println("----------------------------------------");

        myMessage = new EncryptDecorator(myMessage);
        System.out.println("2. Після шифрування:\n'" + myMessage.getContent() + "'");
        System.out.println("----------------------------------------");

        myMessage = new TimestampDecorator(myMessage);
        System.out.println("3. Після додавання дати:\n'" + myMessage.getContent() + "'");
        System.out.println("----------------------------------------");

        myMessage = new AuthorDecorator(myMessage, "Бережняк І.Ю.");
        System.out.println("4. Фінальне повідомлення (з автором):\n'" + myMessage.getContent() + "'");
        System.out.println("----------------------------------------");
        System.out.println("\n*** Інша комбінація (Стискання + Автор) ***");

        Message anotherMessage = new SimpleMessage("  Ще   одне    повідомлення. ");
        System.out.println("Базове:\n'" + anotherMessage.getContent() + "'");

        anotherMessage = new CompressDecorator(anotherMessage);
        System.out.println("Після стискання:\n'" + anotherMessage.getContent() + "'");

        anotherMessage = new AuthorDecorator(anotherMessage, "Бережняк І.Ю.");
        System.out.println("Фінальне (стиснуте + автор):\n'" + anotherMessage.getContent() + "'");
    }
}
