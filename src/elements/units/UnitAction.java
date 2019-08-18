package elements.units;

import elements.ElementAction;

public interface UnitAction extends ElementAction {
    void setHP(int hp);
    int getHP();
    void setDEF(int def);
    int getDEF();
    void setNombre(String nombre);
    String getNombre();
}
