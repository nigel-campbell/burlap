package burlap.mdp.stochasticgames.model;

import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.state.State;
import burlap.mdp.stochasticgames.action.JointAction;

import java.util.ArrayList;
import java.util.List;

/**
 * The interface and comment mechanisms for defining
 * a stochastic game's state transition dynamics.
 * 
 * @author James MacGlashan
 *
 */
public interface JointActionModel {

	
	/**
	 * Samples the result of performing {@link JointAction} ja in {@link State} s.
	 * @param s the state in which the joint action is performed.
	 * @param ja the joint action to be performed
	 * @return a sample from the resulting state.
	 */
	State sample(State s, JointAction ja);

	
	/**
	 * Returns the transition probabilities for applying the provided {@link JointAction} action in the given state.
	 * Transition probabilities are specified as list of {@link StateTransitionProb} objects. The list
	 * is only required to contain transitions with non-zero probability.
	 * @param s the state in which the joint action is performed
	 * @param ja the joint action performed
	 * @return a list of state {@link StateTransitionProb} objects.
	 */
	List<StateTransitionProb> stateTransitions(State s, JointAction ja);
	

	


	class Helper{

		/**
		 * A helper method for deterministic transition dynamics. This method will return a list containing
		 * one {@link StateTransitionProb} object which is assigned probability 1
		 * and whose state is determined by querying the {@link #sample(State, JointAction)}
		 * method.
		 * @param s the state in which the joint action would be executed
		 * @param ja the joint action to be performed in the state.
		 * @return a list containing one {@link StateTransitionProb} object which is assigned probability 1
		 */
		public static List<StateTransitionProb> deterministicTransition(JointActionModel model, State s, JointAction ja){
			List <StateTransitionProb> res = new ArrayList<StateTransitionProb>();
			State sp = model.sample(s, ja);
			StateTransitionProb tp = new StateTransitionProb(sp, 1.);
			res.add(tp);
			return res;
		}
	}
	


}
