public interface Lifeforms {
    
    abstract int [] getStats();
    abstract String getName();
    abstract int getLocation();
    abstract Inventory getInventory();
    abstract int getHealth();
    abstract int getMana();
    abstract int attack(Lifeforms target, World currentWorld);
    abstract int accuracy(Lifeforms target);
    abstract int getLevel();
    
}
