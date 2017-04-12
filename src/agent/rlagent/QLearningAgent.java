package agent.rlagent;

import java.util.*;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Renvoi 0 pour valeurs initiales de Q
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
	/**
	 *  format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
	 */
	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;
	
	//AU CHOIX: vous pouvez utiliser une Map avec des Pair pour les clés si vous préférez
	//protected HashMap<Pair<Etat,Action>,Double> qvaleurs;

	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param _env
	 * @param nbS attention ici il faut tous les etats (meme obstacles) car Q avec tableau ...
	 * @param nbA
	 */
	public QLearningAgent(double alpha, double gamma, Environnement _env) {
		super(alpha, gamma,_env);
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();
	}


	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  (plusieurs actions sont renvoyees si valeurs identiques)
	 *  renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)
	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		// retourne action de meilleures valeurs dans _e selon Q : utiliser getQValeur()
		// retourne liste vide si aucune action legale (etat terminal)
		List<Action> returnactions = new ArrayList<Action>();
		if (this.getActionsLegales(e).size() == 0){//etat  absorbant; impossible de le verifier via environnement
			System.out.println("aucune action legale");
			return new ArrayList<Action>();
			
		}
		
		return returnactions;
		
		
	}
	
	@Override
	public double getValeur(Etat e) {
		Double maximum = 0.0;
		if(qvaleurs.get(e) != null) {
			Iterator<Action> actions = qvaleurs.get(e).keySet().iterator();
			while (actions.hasNext()) {
				Action action = actions.next();
				if (qvaleurs.get(e).get(action) != null) {
					if (qvaleurs.get(e).get(action) > maximum) {
						maximum = qvaleurs.get(e).get(action);
					}
				}
			}
		}
		return maximum;
	}

	@Override
	public double getQValeur(Etat e, Action a) {
		try  {
			Double qValeurEtat = this.qvaleurs.get(e).get(a);
			return qValeurEtat;
		} catch (Exception excpt) {
			return 0;
		}
	}
	
	
	
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		HashMap<Action, Double> listeAction = this.qvaleurs.get(e);
		if(listeAction != null) {
			Double val = listeAction.get(a);
			if(val != null) this.qvaleurs.get(e).remove(a);

			this.qvaleurs.get(e).put(a, d);

		}
		else {
			HashMap<Action, Double> soulevade = new HashMap<Action, Double>();
			soulevade.put(a, new Double(d));
			this.qvaleurs.put(e, soulevade);
		}

		if(d > vmax) vmax = d;
		else if(d < vmin) vmin = d;

		this.notifyObs();
		
	}
	
	
	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {


		if (RLAgent.DISPRL)
			System.out.println("QL mise a jour etat "+e+" action "+a+" etat' "+esuivant+ " r "+reward);

		double qval = (1 - this.alpha) * getQValeur(e, a) + this.alpha * (reward + this.gamma * this.getValeur(esuivant));
		this.setQValeur(e, a, qval);
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	@Override
	public void reset() {
		super.reset();
		//*** VOTRE CODE
		
		this.episodeNb =0;
		this.notifyObs();
	}









	


}
