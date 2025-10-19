import java.util.*;

interface Printable {
    void print();
}

abstract class Figure implements Printable {
    public abstract double calculateArea();
    public abstract double calculatePerimeter();
}

class Triangle extends Figure {
    private double a, b, c;

    public Triangle(double a, double b, double c) {
        if (a <= 0 || b <= 0 || c <= 0 || a + b <= c || a + c <= b || b + c <= a)
            throw new IllegalArgumentException("Invalid triangle sides");
        this.a = a; this.b = b; this.c = c;
    }

    @Override
    public double calculateArea() {
        double p = (a + b + c) / 2.0;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    @Override
    public double calculatePerimeter() {
        return a + b + c;
    }

    @Override
    public void print() {
        System.out.printf("Triangle: a=%.2f b=%.2f c=%.2f%n", a, b, c);
        System.out.printf("Area=%.2f  Perimeter=%.2f%n", calculateArea(), calculatePerimeter());
    }
}

class Square extends Figure {
    private double side;

    public Square(double side) {
        if (side <= 0) throw new IllegalArgumentException("Invalid side");
        this.side = side;
    }

    @Override
    public double calculateArea() {
        return side * side;
    }

    @Override
    public double calculatePerimeter() {
        return 4 * side;
    }

    @Override
    public void print() {
        System.out.printf("Square: side=%.2f%n", side);
        System.out.printf("Area=%.2f  Perimeter=%.2f%n", calculateArea(), calculatePerimeter());
    }
}

class Circle extends Figure {
    private double r;

    public Circle(double r) {
        if (r <= 0) throw new IllegalArgumentException("Invalid radius");
        this.r = r;
    }

    @Override
    public double calculateArea() {
        return Math.PI * r * r;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * r;
    }

    @Override
    public void print() {
        System.out.printf("Circle: r=%.2f%n", r);
        System.out.printf("Area=%.2f  Perimeter=%.2f%n", calculateArea(), calculatePerimeter());
    }
}

class Prism implements Printable {
    private Figure base;
    private double height;

    public Prism(Figure base, double height) {
        if (height <= 0) throw new IllegalArgumentException("Invalid height");
        this.base = base;
        this.height = height;
    }

    public double calculateVolume() {
        return base.calculateArea() * height;
    }

    public double calculateSurfaceArea() {
        return 2 * base.calculateArea() + base.calculatePerimeter() * height;
    }

    @Override
    public void print() {
        System.out.println("Prism base:");
        base.print();
        System.out.printf("Height=%.2f%n", height);
        System.out.printf("Volume=%.2f  Surface=%.2f%n", calculateVolume(), calculateSurfaceArea());
    }
}

class InputHandler {
    private final Scanner sc;

    public InputHandler(Scanner sc) {
        this.sc = sc;
    }

    public Figure inputFigureData(int type) {
        try {
            switch (type) {
                case 1 -> {
                    System.out.print("Enter a b c: ");
                    double a = sc.nextDouble();
                    double b = sc.nextDouble();
                    double c = sc.nextDouble();
                    return new Triangle(a, b, c);
                }
                case 2 -> {
                    System.out.print("Enter side: ");
                    double s = sc.nextDouble();
                    return new Square(s);
                }
                case 3 -> {
                    System.out.print("Enter radius: ");
                    double r = sc.nextDouble();
                    return new Circle(r);
                }
                default -> {
                    System.out.println("Invalid type");
                    return null;
                }
            }
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Invalid input");
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Prism inputPrismData(int baseType) {
        Figure base = inputFigureData(baseType);
        if (base == null) return null;
        System.out.print("Enter prism height: ");
        try {
            double h = sc.nextDouble();
            return new Prism(base, h);
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Invalid input");
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InputHandler ih = new InputHandler(sc);

        Integer currentType = null;
        boolean isPrism = false;
        Figure figure = null;
        Prism prism = null;

        while (true) {
            System.out.print("\nMenu: 1-Choose figure  2-Enter data  3-Display  4-Exit: ");
            int opt = safeReadInt(sc);

            switch (opt) {
                case 1 -> {
                    System.out.print("Select: 1-Flat figure  2-Prism: ");
                    int figType = safeReadInt(sc);
                    if (figType == 1) {
                        System.out.print("Choose flat figure: 1-Triangle  2-Square  3-Circle: ");
                        currentType = safeReadInt(sc);
                        isPrism = false;
                        System.out.println("Flat figure selected.");
                    } else if (figType == 2) {
                        System.out.print("Choose prism base: 1-Triangle  2-Square  3-Circle: ");
                        currentType = safeReadInt(sc);
                        isPrism = true;
                        System.out.println("Prism selected.");
                    } else {
                        System.out.println("Invalid option");
                    }
                }

                case 2 -> {
                    if (currentType == null) {
                        System.out.println("Choose figure first (option 1).");
                        break;
                    }

                    if (isPrism) {
                        Prism p = ih.inputPrismData(currentType);
                        if (p != null) {
                            prism = p;
                            figure = null;
                        }
                    } else {
                        Figure f = ih.inputFigureData(currentType);
                        if (f != null) {
                            figure = f;
                            prism = null;
                        }
                    }
                }

                case 3 -> {
                    if (isPrism && prism != null)
                        prism.print();
                    else if (!isPrism && figure != null)
                        figure.print();
                    else
                        System.out.println("No data to display.");
                }

                case 4 -> {
                    System.out.println("Bye!");
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static int safeReadInt(Scanner sc) {
        try {
            return sc.nextInt();
        } catch (InputMismatchException e) {
            sc.nextLine();
            return -1;
        }
    }
}
