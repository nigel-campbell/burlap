package burlap.behavior.singleagent.planning.deterministic.informed.astar;

import burlap.behavior.singleagent.planning.deterministic.informed.Heuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.PrioritizedSearchNode;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.Action;
import burlap.mdp.singleagent.RewardFunction;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.statehashing.HashableState;
import burlap.mdp.statehashing.HashableStateFactory;


/**
 * A weighted greedy search in which the g(n) function (the cost to the current node) is weighted by a fraction 0 &lt;= w &lt;= 1.
 * When w = 0, the search is fully greedy. When w = 1, the search is optimal and equivalent to A*.
 * 
 * <p>
 * If a terminal function is provided via the setter method defined for OO-MDPs, then the search algorithm will not expand any nodes
 * that are terminal states, as if there were no actions that could be executed from that state. Note that terminal states
 * are not necessarily the same as goal states, since there could be a fail condition from which the agent cannot act, but
 * that is not explicitly represented in the transition dynamics.
 * 
 * @author James MacGlashan
 *
 */
public class WeightedGreedy extends AStar {
	
	
	/**
	 * The cost function weight.
	 */
	protected double costWeight;
	

	/**
	 * Initializes the valueFunction.
	 * @param domain the domain in which to plan
	 * @param rf the reward function that represents costs as negative reward
	 * @param gc should evaluate to true for goal states; false otherwise
	 * @param hashingFactory the state hashing factory to use
	 * @param heuristic the planning heuristic. Should return non-positive values.
	 * @param costWeight a fraction 0 &lt;= w &lt;= 1. When w = 0, the search is fully greedy. When w = 1, the search is optimal and equivalent to A*.
	 */
	public WeightedGreedy(SADomain domain, RewardFunction rf, StateConditionTest gc, HashableStateFactory hashingFactory, Heuristic heuristic, double costWeight) {
		super(domain, gc, hashingFactory, heuristic);
		this.costWeight = costWeight;
	}
	
	
	@Override
	public double computeF(PrioritizedSearchNode parentNode, Action generatingAction, HashableState successorState, double r) {
		double cumR = 0.;
		if(parentNode != null){
			double pCumR = cumulatedRewardMap.get(parentNode.s);
			cumR = pCumR + r;
		}
		
		double H  = heuristic.h(successorState.s);
		lastComputedCumR = cumR;
		double F = (this.costWeight * cumR) + H;
		
		return F;
	}

}
