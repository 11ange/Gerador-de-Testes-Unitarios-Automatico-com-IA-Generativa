/**
 * FinancialCalculator.java
 * Implementação de uma calculadora financeira com diversos métodos para cálculos financeiros comuns
 * 
 * @author Exemplo para Gerador de Testes Unitários
 * @version 1.0
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class FinancialCalculator {
    
    private static final int DECIMAL_PLACES = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    
    /**
     * Calcula o valor futuro de um investimento com juros compostos
     * 
     * @param principal Valor inicial investido
     * @param rate Taxa de juros anual (em decimal, ex: 0.05 para 5%)
     * @param timeYears Tempo em anos
     * @param periodicContribution Contribuição periódica anual (opcional)
     * @return Valor futuro do investimento
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    public BigDecimal calculateFutureValue(BigDecimal principal, BigDecimal rate, 
                                          int timeYears, BigDecimal periodicContribution) {
        // Validação dos parâmetros
        if (principal == null || rate == null || periodicContribution == null) {
            throw new IllegalArgumentException("Os parâmetros não podem ser nulos");
        }
        
        if (principal.compareTo(BigDecimal.ZERO) < 0 || 
            rate.compareTo(BigDecimal.ZERO) < 0 || 
            timeYears < 0 ||
            periodicContribution.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Os parâmetros devem ser valores não negativos");
        }
        
        // Cálculo do valor futuro do principal
        BigDecimal onePlusRate = BigDecimal.ONE.add(rate);
        BigDecimal compoundFactor = onePlusRate.pow(timeYears);
        BigDecimal futureValuePrincipal = principal.multiply(compoundFactor);
        
        // Cálculo do valor futuro das contribuições periódicas
        BigDecimal futureValueContributions = BigDecimal.ZERO;
        if (periodicContribution.compareTo(BigDecimal.ZERO) > 0) {
            if (rate.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal rateFactorMinusOne = compoundFactor.subtract(BigDecimal.ONE);
                futureValueContributions = periodicContribution.multiply(rateFactorMinusOne.divide(rate, 10, ROUNDING_MODE));
            } else {
                // Se a taxa for zero, é apenas a multiplicação direta
                futureValueContributions = periodicContribution.multiply(new BigDecimal(timeYears));
            }
        }
        
        // Soma e arredondamento
        return futureValuePrincipal.add(futureValueContributions).setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Sobrecarga do método calculateFutureValue sem contribuições periódicas
     */
    public BigDecimal calculateFutureValue(BigDecimal principal, BigDecimal rate, int timeYears) {
        return calculateFutureValue(principal, rate, timeYears, BigDecimal.ZERO);
    }
    
    /**
     * Calcula o valor da prestação de um empréstimo com base no sistema de amortização constante
     * 
     * @param loanAmount Valor total do empréstimo
     * @param annualInterestRate Taxa de juros anual (em decimal)
     * @param loanTermYears Prazo do empréstimo em anos
     * @return Valor da prestação mensal
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    public BigDecimal calculateLoanPayment(BigDecimal loanAmount, BigDecimal annualInterestRate, int loanTermYears) {
        // Validação dos parâmetros
        if (loanAmount == null || annualInterestRate == null) {
            throw new IllegalArgumentException("Os parâmetros não podem ser nulos");
        }
        
        if (loanAmount.compareTo(BigDecimal.ZERO) <= 0 || 
            annualInterestRate.compareTo(BigDecimal.ZERO) <= 0 || 
            loanTermYears <= 0) {
            throw new IllegalArgumentException("Os parâmetros devem ser valores positivos");
        }
        
        // Converter taxa de juros anual para mensal
        BigDecimal monthlyRate = annualInterestRate.divide(new BigDecimal("12"), 10, ROUNDING_MODE);
        
        // Converter prazo em anos para meses
        int termMonths = loanTermYears * 12;
        
        // Cálculo do fator de amortização
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal numerator = monthlyRate.multiply(onePlusRate.pow(termMonths));
        BigDecimal denominator = onePlusRate.pow(termMonths).subtract(BigDecimal.ONE);
        BigDecimal amortizationFactor = numerator.divide(denominator, 10, ROUNDING_MODE);
        
        // Cálculo da prestação
        return loanAmount.multiply(amortizationFactor).setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Calcula o retorno sobre investimento (ROI)
     * 
     * @param initialInvestment Valor inicial investido
     * @param finalValue Valor final do investimento
     * @return ROI em percentual
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    public BigDecimal calculateROI(BigDecimal initialInvestment, BigDecimal finalValue) {
        // Validação dos parâmetros
        if (initialInvestment == null || finalValue == null) {
            throw new IllegalArgumentException("Os parâmetros não podem ser nulos");
        }
        
        if (initialInvestment.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O investimento inicial deve ser um valor positivo");
        }
        
        // Cálculo do ROI em percentual
        BigDecimal profit = finalValue.subtract(initialInvestment);
        BigDecimal roi = profit.divide(initialInvestment, 10, ROUNDING_MODE).multiply(new BigDecimal("100"));
        
        return roi.setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Calcula a taxa interna de retorno aproximada para fluxos de caixa uniformes
     * 
     * @param initialInvestment Investimento inicial (negativo)
     * @param cashFlows Lista de fluxos de caixa futuros
     * @return Taxa interna de retorno aproximada em percentual
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    public BigDecimal calculateApproximateIRR(BigDecimal initialInvestment, List<BigDecimal> cashFlows) {
        // Validação dos parâmetros
        if (initialInvestment == null || cashFlows == null || cashFlows.isEmpty()) {
            throw new IllegalArgumentException("Os parâmetros não podem ser nulos ou vazios");
        }
        
        if (initialInvestment.compareTo(BigDecimal.ZERO) >= 0) {
            throw new IllegalArgumentException("O investimento inicial deve ser um valor negativo");
        }
        
        // Soma de todos os fluxos de caixa
        BigDecimal totalCashFlow = BigDecimal.ZERO;
        for (BigDecimal cashFlow : cashFlows) {
            if (cashFlow == null) {
                throw new IllegalArgumentException("Os fluxos de caixa não podem conter valores nulos");
            }
            totalCashFlow = totalCashFlow.add(cashFlow);
        }
        
        // Calcula o valor médio dos fluxos de caixa
        BigDecimal averageCashFlow = totalCashFlow.divide(new BigDecimal(cashFlows.size()), 10, ROUNDING_MODE);
        
        // Cálculo aproximado da IRR (pressupõe fluxos de caixa uniformes)
        BigDecimal totalReturn = totalCashFlow.add(initialInvestment);
        BigDecimal yearsCount = new BigDecimal(cashFlows.size());
        BigDecimal annualReturn = totalReturn.divide(yearsCount, 10, ROUNDING_MODE);
        BigDecimal absoluteInitialInvestment = initialInvestment.abs();
        BigDecimal irr = annualReturn.divide(absoluteInitialInvestment, 10, ROUNDING_MODE).multiply(new BigDecimal("100"));
        
        return irr.setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Formata um valor monetário de acordo com a localidade e moeda especificadas
     * 
     * @param amount Valor a ser formatado
     * @param locale Localidade a ser usada na formatação
     * @param currencyCode Código ISO 4217 da moeda
     * @return String formatada do valor monetário
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    public static String formatCurrency(BigDecimal amount, Locale locale, String currencyCode) {
        if (amount == null || locale == null || currencyCode == null || currencyCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Os parâmetros não podem ser nulos ou vazios");
        }
        
        try {
            Currency currency = Currency.getInstance(currencyCode);
            java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance(locale);
            formatter.setCurrency(currency);
            return formatter.format(amount);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Código de moeda inválido: " + currencyCode);
        }
    }
    
    /**
     * Calcula o valor presente de um montante futuro
     * 
     * @param futureValue Valor futuro
     * @param rate Taxa de juros anual (em decimal)
     * @param timeYears Tempo em anos
     * @return Valor presente
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    public BigDecimal calculatePresentValue(BigDecimal futureValue, BigDecimal rate, int timeYears) {
        // Validação dos parâmetros
        if (futureValue == null || rate == null) {
            throw new IllegalArgumentException("Os parâmetros não podem ser nulos");
        }
        
        if (futureValue.compareTo(BigDecimal.ZERO) <= 0 || 
            rate.compareTo(BigDecimal.ZERO) < 0 || 
            timeYears < 0) {
            throw new IllegalArgumentException("Os parâmetros devem ser valores válidos");
        }
        
        // Cálculo do valor presente
        BigDecimal onePlusRate = BigDecimal.ONE.add(rate);
        BigDecimal discountFactor = onePlusRate.pow(timeYears);
        BigDecimal presentValue = futureValue.divide(discountFactor, 10, ROUNDING_MODE);
        
        return presentValue.setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
}