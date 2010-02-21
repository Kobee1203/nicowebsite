package org.nds.johndog.sample.income;

import org.nds.johndog.sample.income.exceptions.CalcMethodException;
import org.nds.johndog.sample.income.exceptions.PositionException;
import org.nds.johndog.sample.income.method.ICalcMethod;


public class IncomeCalculator{

	private ICalcMethod calcMethod;
	private Position position;

	public void setCalcMethod(ICalcMethod calcMethod){
		this.calcMethod = calcMethod;
	}
	public void setPosition(Position position){
		this.position = position;
	}
	public double calc (){
		if (calcMethod==null){
			throw new CalcMethodException("CalcMethod not yet maintained");
		}
		if (position==null){
			throw new PositionException("Position not yet maintained");
		}
		return calcMethod.calc(position);
	}
}
