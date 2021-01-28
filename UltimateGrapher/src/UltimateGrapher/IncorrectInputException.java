package UltimateGrapher;

public class IncorrectInputException extends Exception{

    /**
     * I have no idea what this is but VS Code is telling me to add this
     */
    private static final long serialVersionUID = 1L;

    /**
     * thrown if the user input is incorrect input should take the form of (2, 1),
     * (5, 8), (4, 5)
     *
     * input requirements: 1. input coordinate count needs to be > 1 2. input domain
     * must not contain two of the same number unless they have the same co-domain
     * 3. each element in the input domain must not have two or more co-domains 4.
     * (more to be added)
     */
    public IncorrectInputException(){
        super("User input is incorrect. The input should look like: (2, 1), (5, 8)");
    }

}