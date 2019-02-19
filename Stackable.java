interface Stackable extends Item
{
    abstract int maxStackAmount();
    abstract int stackAmount();
    abstract int addAmount();
    abstract int removeAmount();
}
