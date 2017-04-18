package keegan.labstuff.entity;

public interface IBubbleProvider
{
    //    public IBubble getBubble();
    float getBubbleSize();

    void setBubbleVisible(boolean shouldRender);

    boolean getBubbleVisible();
}