package hao.LR.entity;

import java.util.ArrayList;

public class Vector_vote extends Vector {

	private double logvote;
	private double vote;
	
	public Vector_vote(double vote,double[] features, String lable) {
		super(features, lable);
		this.vote=vote;
		this.logvote=Math.log(vote);
	}
	
	@Override
	public String toString() {
		return "Vector_vote [logvote=" + logvote + ", vote=" + vote + "]";
	}

	public double getVote() {
		return vote;
	}

	public void setVote(double vote) {
		this.vote = vote;
	}

	public double getLogvote() {
		return logvote;
	}

	public void setLogvote(double logvote) {
		this.logvote = logvote;
	}

	/**
	 * 只要lable是正的情况
	 * @param v2
	 * @return
     */
	public ArrayList<Vector_vote> compare(Vector_vote v2){
		//Vector vr[]  = new Vector[2];
		ArrayList<Vector_vote> vre = new ArrayList<>();
		int leb1 = Integer.parseInt(this.getLable());
		int leb2 = Integer.parseInt(v2.getLable());	
		if(leb1==leb2&&leb1==1){
			double vote1 = this.getVote();
			double vote2 = v2.getVote();
			if(vote1>vote2){
				vre.add(new Vector_vote(vote1-vote2,featureSUB(this.getFeatures(),v2.getFeatures()),"1"));
			}else if(vote1<vote2){
				vre.add(new Vector_vote(vote2-vote1,featureSUB(this.getFeatures(),v2.getFeatures()),"1"));
			}
		}else if(leb1>leb2){
			vre.add(new Vector_vote(Math.E,featureSUB(this.getFeatures(),v2.getFeatures()),"1"));
			vre.add(new Vector_vote(Math.E,featureSUB(v2.getFeatures(),this.getFeatures()),"0"));
		}else if(leb2>leb1){
			vre.add(new Vector_vote(Math.E,featureSUB(this.getFeatures(),v2.getFeatures()),"0"));
			vre.add(new Vector_vote(Math.E,featureSUB(v2.getFeatures(),this.getFeatures()),"1"));
		}
		return vre;
	}

	/**
	 * 两行数组相减
	 * @param fea1
	 * @param fea2
	 * @return
	 */
	private double[] featureSUB(double fea1[],double fea2[]){
		double re[] = new double[fea1.length];
		for (int i = 0; i < fea1.length; i++) {
			//System.out.println(fea2.length);
			re[i]=fea1[i]-fea2[i];
		}
		return re;
	}
}




