package k23cnt2.nmt.day02.tight_loosely_coupling;

public class LooselyCoupledService {

    private SortAlgorithm sortAlgorithm;

    public LooselyCoupledService() {}

    public LooselyCoupledService(SortAlgorithm sortAlgorithm) {
        this.sortAlgorithm = sortAlgorithm;
    }

    public void complexBusiness(int[] array) {
        sortAlgorithm.sort(array);
    }

    public static void main(String[] args) {

        // Tạo service với thuật toán BubbleSort (loose coupling)
        LooselyCoupledService service =
                new LooselyCoupledService(new LooselyBubbleSortAlgorithm());

        // Gọi xử lý
        service.complexBusiness(new int[]{11, 21, 13, 42, 15});
    }
}
