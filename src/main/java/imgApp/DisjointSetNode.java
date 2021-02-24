package imgApp;

public class DisjointSetNode<I> {
    public DisjointSetNode<?> parent = null;
    public I data;

    public DisjointSetNode(I data){
        this.data=data;
}

    public I getData() {
        return data;
    }

    public void setData(I data) {
        this.data = data;
    }
}
