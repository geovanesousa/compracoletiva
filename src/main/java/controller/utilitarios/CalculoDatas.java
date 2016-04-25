package controller.utilitarios;

import java.util.Calendar;

public class CalculoDatas {

	public Integer diasEntreDatas(Calendar outraData) {
		Calendar data = outraData;
		Calendar hoje = Calendar.getInstance();
		int resultado = hoje.compareTo(data);
		Long diferenca = 0L;
		if (resultado > 0) {
			diferenca = (hoje.getTimeInMillis() / 86400000) - (data.getTimeInMillis() / 86400000);

		}
		Integer dias = Integer.parseInt(String.valueOf(diferenca));
		return dias;
	}

	public String dataAtual() {
		// aaaa-mm-dd
		Calendar c = Calendar.getInstance();
		String ano, mes, dia, data;
		ano = String.valueOf(c.get(Calendar.YEAR));
		int intMes = c.get(Calendar.MONTH);
		intMes = intMes + 1; 
		mes = String.valueOf(intMes);
		dia = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		data = ano + "-" + mes + "-" + dia;
		return data;
	}

}
