/* Cada aresta armazena:
    A coordenada x do vértice mais baixo;
    A coordenada y do vértice mais alto;
    A medida x(i + 1) - x(i): delta(x) da aresta;
*/

public class Edge {

    private int x;
    private int y;
    private int dx;
    
    public Edge(int x, int y, int dx) {
        this.x = x;
        this.y = y;
        this.dx = dx;
    }

    public int getDx() {
        return this.dx;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void increaseX() {
        this.x += this.dx;
    }
}