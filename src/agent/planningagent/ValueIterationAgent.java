package agent.planningagent;

import java.util.*;

import util.HashMapUtil;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;

    /**
     * Liste des actions de l'agent
     */
    protected Map<Etat, List<Action>> returnactions = new HashMap<Etat, List<Action>>();
	
	/**
	 * 
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		V = new HashMap<Etat,Double>();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
            this.returnactions.put(etat, new ArrayList<Action>());
		}
		this.notifyObs();
		
	}
	
	
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
		//delta < epsilon 
		this.delta=0.0;

        this.vmax = 0;
        this.vmin = 1;

		Iterator<Etat> youpi = this.V.keySet().iterator();
		while(youpi.hasNext()) {
            Etat etat = youpi.next();
            double val = this.V.get(etat);
		    if(val > vmax) vmax = val;
		    else if(val < vmin) vmin = val;
        }


		this.bellman();
        // mise a jour vmax et vmin pour affichage du gradient de couleur:
		//vmax est la valeur  max de V  pour tout s
		//vmin est la valeur min de V  pour tout s
		// ...

		//******************* laisser la notification a la fin de la methode	
		this.notifyObs();
	}

	public void bellman() {
        double somme = 0.0;
        double calcul = 0;

        Map<Etat, Double> update = new HashMap<Etat, Double>();
        //*** VOTRE CODE
        for(Etat etat : this.mdp.getEtatsAccessibles()) {
            for(Action action : this.mdp.getActionsPossibles(etat)) {
                calcul = 0;
                try {
                    Map<Etat, Double> map = this.mdp.getEtatTransitionProba(etat, action);
                    Iterator<Etat> iterator = map.keySet().iterator();
                    while(iterator.hasNext()) {
                        Etat etat1 = iterator.next();
                        calcul += map.get(etat1) * (this.mdp.getRecompense(etat, action, etat1) + (getGamma() * this.V.get(etat1)));
                    }
                    if(calcul > somme) {
                        if(this.returnactions.get(etat).isEmpty()) this.returnactions.get(etat).add(action);
                        else this.returnactions.get(etat).set(0, action);
                        somme = calcul;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            update.put(etat, somme);
            somme = 0;
        }

        Iterator<Etat> etatIterator = update.keySet().iterator();
        while (etatIterator.hasNext()) {
            Etat etat = etatIterator.next();
            this.getV().replace(etat, update.get(etat));
        }
    }
	
	/**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		List<Action> actions = this.getPolitique(e);
		if (actions.size() == 0)
			return Action2D.NONE;
		else{//choix aleatoire
			return actions.get(rand.nextInt(actions.size()));
		}

		
	}
	@Override
	public double getValeur(Etat _e) {
		return  V.get(_e);
	}
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans etat 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		// retourne action de meilleure valeur dans _e selon V, 
		// retourne liste vide si aucune action legale (etat absorbant)
		return this.returnactions.get(_e);
	}
	
	@Override
	public void reset() {
		super.reset();

		
		this.V.clear();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
	}

	

	

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}


	
	

	
}
