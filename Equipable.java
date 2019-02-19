interface Equipable extends Item
{
    abstract String getSlotId();
    abstract String getItemStats();
    abstract boolean canEquit(String currentStats);
    abstract boolean isEquip();
}
