package pacman.environnementRL;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import pacman.elements.MazePacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	private ArrayList<Point> positionPacmans = new ArrayList<Point>();

	private ArrayList<Point> positionDots = new ArrayList<Point>();

	private ArrayList<Point> positionGhosts = new ArrayList<Point>();
	
	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){

		for(int i=0; i<_stategamepacman.getNumberOfGhosts(); i++) {
			positionGhosts.add(new Point(_stategamepacman.getGhostState(i).getX(), _stategamepacman.getGhostState(i).getY()));
		}

		for(int i=0; i<_stategamepacman.getNumberOfPacmans(); i++) {
			positionPacmans.add(new Point(_stategamepacman.getPacmanState(i).getX(), _stategamepacman.getPacmanState(i).getY()));
		}

		MazePacman m = _stategamepacman.getMaze();
		for(int i=0; i<m.getSizeX(); i++) {
			for(int j=0; j<m.getSizeY(); j++) {
				if(m.isFood(i, j)) {
					positionDots.add(new Point(i, j));
				}
			}
		}

		
	}
	
	@Override
	public String toString() {
		
		return "";
	}
	
	
	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		


		// on renvoie le clone
		return clone;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;

		if (!positionPacmans.equals(that.positionPacmans)) return false;
		if (!positionDots.equals(that.positionDots)) return false;
		return positionGhosts.equals(that.positionGhosts);
	}

	@Override
	public int hashCode() {
		int result = positionPacmans.hashCode();
		result = 31 * result + positionDots.hashCode();
		result = 31 * result + positionGhosts.hashCode();
		return result;
	}
}
