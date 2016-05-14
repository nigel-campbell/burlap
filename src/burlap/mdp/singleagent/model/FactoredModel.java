package burlap.mdp.singleagent.model;

import burlap.mdp.core.Action;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.RewardFunction;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import burlap.mdp.singleagent.model.statemodel.SampleStateModel;
import burlap.mdp.singleagent.model.statemodel.StateTransitionProb;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James MacGlashan.
 */
public class FactoredModel implements TaskFactoredModel, FullModel{
	protected SampleStateModel stateModel;
	protected RewardFunction rf;
	protected TerminalFunction tf;

	public FactoredModel() {
	}

	public FactoredModel(SampleStateModel stateModel, RewardFunction rf, TerminalFunction tf) {
		this.stateModel = stateModel;
		this.rf = rf;
		this.tf = tf;
	}

	@Override
	public void useRewardFunction(RewardFunction rf) {
		this.rf = rf;
	}

	@Override
	public void useTerminalFunction(TerminalFunction tf) {
		this.tf = tf;
	}

	@Override
	public RewardFunction rewardFunction() {
		return rf;
	}

	@Override
	public TerminalFunction terminalFunction() {
		return tf;
	}



	@Override
	public EnvironmentOutcome sampleTransition(State s, Action a) {

		State sprime = this.stateModel.sampleStateTransition(s, a);
		double r = this.rf.reward(s, a, sprime);
		boolean t = this.tf.isTerminal(sprime);

		EnvironmentOutcome eo = new EnvironmentOutcome(s, a, sprime, r, t);

		return eo;
	}

	@Override
	public List<TransitionProb> transitions(State s, Action a) {

		if(!(this.stateModel instanceof FullStateModel)){
			throw new RuntimeException("Factored Model cannot enumerate transition distribution, because the state model does not implement FullStateModel");
		}

		List<StateTransitionProb> stps = ((FullStateModel)this.stateModel).stateTransitions(s, a);
		List<TransitionProb> tps = new ArrayList<TransitionProb>(stps.size());
		for(StateTransitionProb stp : stps){
			double r = this.rf.reward(s, a, stp.s);
			boolean t = this.tf.isTerminal(stp.s);
			TransitionProb tp = new TransitionProb(stp.p, new EnvironmentOutcome(s, a, stp.s, r, t));
			tps.add(tp);
		}

		return tps;
	}

	public SampleStateModel getStateModel() {
		return stateModel;
	}

	public void setStateModel(SampleStateModel stateModel) {
		this.stateModel = stateModel;
	}

	public RewardFunction getRf() {
		return rf;
	}

	public void setRf(RewardFunction rf) {
		this.rf = rf;
	}

	public TerminalFunction getTf() {
		return tf;
	}

	public void setTf(TerminalFunction tf) {
		this.tf = tf;
	}

	@Override
	public boolean terminalState(State s) {
		return this.tf.isTerminal(s);
	}
}
