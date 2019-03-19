interface Usable extends Item
{
    abstract String useEffect();
    abstract int cooldownTimer();
    abstract boolean canUse(String currentStats);
}
