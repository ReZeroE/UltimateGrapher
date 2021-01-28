package UltimateGrapher;

/**
 * a pretty intuitive coordinate class
 * has two attributes of x and y
 *
 * Note: coordinates are doubles not ints
 */
public class Coordinates {

    //field
    private double x;
    private double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    // your favorite equals method
    public boolean equals(Coordinates coord){
        if(this == coord){
            return true;
        }
        if(coord == null){
            return false;
        }
        if(this.getClass().equals(coord.getClass())){
            if(this.getX() == coord.getX() && this.getY() == coord.getY()){
                return true;
            }
        }
        return false;
    }

    /**
     * toString method that prints out the designated coordinate.
     * Note: when printing an array of coordinates, an for loop and the removal of the last ", " section is needed
     */
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("(");
        str.append(this.getX());
        str.append(", ");
        str.append(this.getY());
        str.append("), ");

        return str.toString();
    }

}