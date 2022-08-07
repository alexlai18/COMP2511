package average;

public class Average {
    /**
     * Returns the average of an array of numbers
     * 
     * @param the array of integer numbers
     * @return the average of the numbers
     */
    public float computeAverage(int[] nums) {
        float result = 0;
        float sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }
        result = (sum / (float) nums.length);
        return result;
    }

    public static void main(String[] args) {
        int[] num_array = new int[6];
        for (int i = 0; i < 6; i++) {
            num_array[i] = i + 1;
        }
        Average avg = new Average();
        float new_average = avg.computeAverage(num_array);
        System.out.println("The average is " + new_average);
    }
}
