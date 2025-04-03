/**
 * Calculadora Financeira
 * Implementa funções de cálculos financeiros comuns
 * @author Exemplo para Gerador de Testes Unitários
 */

class FinancialCalculator {
  /**
   * Calcula o valor futuro de um investimento
   * @param {number} principal - Valor inicial investido
   * @param {number} rate - Taxa de juros anual (em decimal, ex: 0.05 para 5%)
   * @param {number} time - Tempo em anos
   * @param {number} [contributions=0] - Contribuições periódicas anuais
   * @return {number} Valor futuro do investimento
   */
  calculateFutureValue(principal, rate, time, contributions = 0) {
    if (principal < 0 || rate < 0 || time < 0 || contributions < 0) {
      throw new Error("Todos os parâmetros devem ser valores não negativos");
    }
    
    // Cálculo do valor futuro com juros compostos
    const futureValuePrincipal = principal * Math.pow(1 + rate, time);
    
    // Cálculo das contribuições periódicas (assumindo final de período)
    const futureValueContributions = contributions * 
      ((Math.pow(1 + rate, time) - 1) / rate);
    
    return Number((futureValuePrincipal + futureValueContributions).toFixed(2));
  }
  
  /**
   * Calcula o valor da prestação de um empréstimo
   * @param {number} loanAmount - Valor total do empréstimo
   * @param {number} annualInterestRate - Taxa de juros anual (em decimal)
   * @param {number} loanTermYears - Prazo do empréstimo em anos
   * @return {number} Valor da prestação mensal
   */
  calculateLoanPayment(loanAmount, annualInterestRate, loanTermYears) {
    if (loanAmount <= 0 || annualInterestRate <= 0 || loanTermYears <= 0) {
      throw new Error("Os parâmetros devem ser valores positivos");
    }
    
    // Converter taxa de juros anual para mensal
    const monthlyRate = annualInterestRate / 12;
    
    // Converter prazo em anos para meses
    const termMonths = loanTermYears * 12;
    
    // Cálculo da prestação
    const payment = loanAmount * 
      (monthlyRate * Math.pow(1 + monthlyRate, termMonths)) / 
      (Math.pow(1 + monthlyRate, termMonths) - 1);
    
    return Number(payment.toFixed(2));
  }
  
  /**
   * Calcula o retorno sobre investimento (ROI)
   * @param {number} initialInvestment - Valor inicial investido
   * @param {number} finalValue - Valor final do investimento
   * @return {number} ROI em percentual
   */
  calculateROI(initialInvestment, finalValue) {
    if (initialInvestment <= 0) {
      throw new Error("O investimento inicial deve ser um valor positivo");
    }
    
    const roi = ((finalValue - initialInvestment) / initialInvestment) * 100;
    return Number(roi.toFixed(2));
  }
  
  /**
   * Calcula o valor presente de um montante futuro
   * @param {number} futureValue - Valor futuro
   * @param {number} rate - Taxa de juros anual (em decimal)
   * @param {number} time - Tempo em anos
   * @return {number} Valor presente
   */
  calculatePresentValue(futureValue, rate, time) {
    if (futureValue <= 0 || rate < 0 || time < 0) {
      throw new Error("Os parâmetros devem ser valores válidos");
    }
    
    const presentValue = futureValue / Math.pow(1 + rate, time);
    return Number(presentValue.toFixed(2));
  }
  
  /**
   * Calcula a taxa interna de retorno simples
   * @param {number} initialInvestment - Investimento inicial
   * @param {number} cashFlow - Fluxo de caixa anual constante
   * @param {number} years - Número de anos
   * @return {number} Taxa interna de retorno em percentual
   */
  calculateSimpleIRR(initialInvestment, cashFlow, years) {
    if (initialInvestment <= 0 || years <= 0) {
      throw new Error("O investimento inicial e anos devem ser valores positivos");
    }
    
    // Cálculo de IRR simples (aproximação)
    const totalCashFlow = cashFlow * years;
    const averageAnnualReturn = (totalCashFlow - initialInvestment) / years;
    const irr = (averageAnnualReturn / initialInvestment) * 100;
    
    return Number(irr.toFixed(2));
  }
}

/**
 * Função utilitária para formatar valores monetários
 * @param {number} value - Valor a ser formatado
 * @param {string} [currencyCode='BRL'] - Código da moeda
 * @param {string} [locale='pt-BR'] - Configuração regional
 * @return {string} Valor formatado como moeda
 */
function formatCurrency(value, currencyCode = 'BRL', locale = 'pt-BR') {
  return new Intl.NumberFormat(locale, {
    style: 'currency',
    currency: currencyCode
  }).format(value);
}

/**
 * Calcula a inflação acumulada
 * @param {number} initialValue - Valor inicial
 * @param {number} inflationRate - Taxa de inflação anual (em decimal)
 * @param {number} years - Número de anos
 * @return {number} Valor após inflação acumulada
 */
function calculateInflationImpact(initialValue, inflationRate, years) {
  if (initialValue < 0 || inflationRate < 0 || years < 0) {
    throw new Error("Os parâmetros devem ser valores não negativos");
  }
  
  const adjustedValue = initialValue * Math.pow(1 + inflationRate, years);
  return Number(adjustedValue.toFixed(2));
}

// Exportar para uso em outros módulos
module.exports = {
  FinancialCalculator,
  formatCurrency,
  calculateInflationImpact
};