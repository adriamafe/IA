import aima.search.framework.GoalTest;

/**
 * Created by felix on 02.10.16.
 */

//checks if a the given state is final. Refer to BLState.isFinal() for implementation details
public class BLGoalTest implements GoalTest {

    public BLGoalTest(){}

    @Override
    public boolean isGoalState(Object state) {
        return ((BLState)state).isFinal();
    }
}