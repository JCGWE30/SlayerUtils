package slayerutils.slayerutils.CustomInventories;

public class InnerInventory {
    int width;
    int height;
    int upperLeft;

    public InnerInventory(int width, int height, int upperLeftCorner){
        this.width=width;
        this.height=height;
        this.upperLeft=upperLeftCorner;
    }

    public int convertToInnerSlot(int slot){
        //Add the offset
        //  offset = upperLeft

        //Add the width gap
        int widthGap = (9-width) * (int) Math.floor((double)slot/width);

        //Add the slot
        int finalSlot = upperLeft+widthGap+slot;
        return finalSlot;
    }

    public int getSize(){
        return width*height;
    }

    public boolean canFit(int slot){
        return getSize()>slot;
    }

    public boolean inInner(int slot){
        int upperX = upperLeft % 9;
        int upperY = (int)Math.floor((double)upperLeft/9);

        int x = slot % 9;
        int y = (int)Math.floor((double)slot/9);

        return x >= upperX && x < upperX + width
                && y >= upperY && y < upperY + height;
    }
}
