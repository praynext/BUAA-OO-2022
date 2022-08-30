package expr;

import java.util.ArrayList;

public class Diyfunction implements Function {
    private String name;
    private ArrayList<Factor> arguments;
    private Expr content;

    public Diyfunction(String name) {
        this.name = name;
        this.arguments = new ArrayList<>(3);
    }

    public void addArgument(Factor argument) {
        this.arguments.add(argument);
    }

    public void setContent(Expr content) {
        this.content = content;
    }

    public Expr simplify(Totaldiyfunctions totaldiyfunctions) {
        for (Diyfunction item : totaldiyfunctions.getDiyfunctions()) {
            if (item.name.equals(this.name)) {
                this.setContent(item.content.copy());
                for (int i = 0; i < arguments.size(); i++) {
                    if (((Pow) item.arguments.get(i)).getName().equals("x")) {
                        this.content.substitute("x", this.arguments.get(i));
                    }
                }
                for (int i = 0; i < arguments.size(); i++) {
                    if (((Pow) item.arguments.get(i)).getName().equals("y")) {
                        this.content.substitute("y", this.arguments.get(i));
                    }
                }
                for (int i = 0; i < arguments.size(); i++) {
                    if (((Pow) item.arguments.get(i)).getName().equals("z")) {
                        this.content.substitute("z", this.arguments.get(i));
                    }
                }
                return this.content;
            }
        }
        return null;
    }

    @Override
    public Diyfunction copy() {
        Diyfunction newdiyfunction = new Diyfunction(this.name);
        newdiyfunction.setContent(this.content.copy());
        for (Factor item : this.arguments) {
            newdiyfunction.arguments.add(item.copy());
        }
        return newdiyfunction;
    }

    @Override
    public Factor getRadix() {
        return null;
    }

    @Override
    public int getExp() {
        return 0;
    }
}
