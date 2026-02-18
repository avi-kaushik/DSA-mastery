
class TrappingRainWater {

    // arr = {3,0,2,4}
    public static int unitsStored(int arr[]) {
        int biggestBarUnit = arr[0], totalUnits = 0;

        for (int i = 1; i < arr.length; i++) {
            if (biggestBarUnit >= arr[i]) {
                totalUnits += biggestBarUnit - arr[i];
            } else {
                biggestBarUnit = arr[1];
            }
        }

        return totalUnits;
    }

    public static void main(String[] args) {
        // int arr[] = {3, 0, 1, 4};
        int arr[] = {3,5,2,3};
        System.out.println("Units stored: " + unitsStored(arr));
    }
}
