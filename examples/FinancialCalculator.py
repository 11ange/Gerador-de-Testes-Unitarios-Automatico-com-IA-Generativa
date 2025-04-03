# financial_calculator.py
# Um módulo para cálculos financeiros básicos

class LoanCalculator:
    """
    Classe para cálculos relacionados a empréstimos e financiamentos.
    Fornece métodos para calcular parcelas, juros e amortização.
    """
    
    def __init__(self, interest_rate, loan_term_years):
        """
        Inicializa um calculador de empréstimos.
        
        Args:
            interest_rate (float): Taxa de juros anual (em percentual, ex: 5.5 para 5.5%)
            loan_term_years (int): Prazo do empréstimo em anos
        
        Raises:
            ValueError: Se a taxa de juros for negativa ou o prazo for menor ou igual a zero
        """
        if interest_rate < 0:
            raise ValueError("A taxa de juros não pode ser negativa")
        if loan_term_years <= 0:
            raise ValueError("O prazo do empréstimo deve ser maior que zero")
            
        self.annual_interest_rate = interest_rate
        self.loan_term_years = loan_term_years
        self.monthly_interest_rate = interest_rate / 100 / 12
        self.loan_term_months = loan_term_years * 12
    
    def calculate_monthly_payment(self, loan_amount):
        """
        Calcula o valor da parcela mensal para um empréstimo.
        
        Args:
            loan_amount (float): Valor total do empréstimo
            
        Returns:
            float: Valor da parcela mensal
            
        Raises:
            ValueError: Se o valor do empréstimo for menor ou igual a zero
        """
        if loan_amount <= 0:
            raise ValueError("O valor do empréstimo deve ser maior que zero")
            
        if self.monthly_interest_rate == 0:
            return loan_amount / self.loan_term_months
            
        numerator = loan_amount * self.monthly_interest_rate * (1 + self.monthly_interest_rate) ** self.loan_term_months
        denominator = (1 + self.monthly_interest_rate) ** self.loan_term_months - 1
        
        return numerator / denominator
    
    def generate_amortization_schedule(self, loan_amount):
        """
        Gera uma tabela de amortização para o empréstimo.
        
        Args:
            loan_amount (float): Valor total do empréstimo
            
        Returns:
            list: Lista de dicionários com detalhes de cada pagamento mensal
                 Cada dicionário contém: 
                 - payment_number (int)
                 - payment_amount (float)
                 - principal_paid (float)
                 - interest_paid (float)
                 - remaining_balance (float)
                 
        Raises:
            ValueError: Se o valor do empréstimo for menor ou igual a zero
        """
        if loan_amount <= 0:
            raise ValueError("O valor do empréstimo deve ser maior que zero")
        
        monthly_payment = self.calculate_monthly_payment(loan_amount)
        remaining_balance = loan_amount
        schedule = []
        
        for month in range(1, self.loan_term_months + 1):
            interest_payment = remaining_balance * self.monthly_interest_rate
            principal_payment = monthly_payment - interest_payment
            
            # Ajuste para o último pagamento (para evitar imprecisões de ponto flutuante)
            if month == self.loan_term_months:
                principal_payment = remaining_balance
                monthly_payment = principal_payment + interest_payment
            
            remaining_balance -= principal_payment
            
            # Para evitar valores de saldo negativos muito pequenos devido a erro de arredondamento
            if abs(remaining_balance) < 0.01:
                remaining_balance = 0
            
            payment_details = {
                'payment_number': month,
                'payment_amount': round(monthly_payment, 2),
                'principal_paid': round(principal_payment, 2),
                'interest_paid': round(interest_payment, 2),
                'remaining_balance': round(remaining_balance, 2)
            }
            
            schedule.append(payment_details)
        
        return schedule
    
    def calculate_total_interest(self, loan_amount):
        """
        Calcula o total de juros pagos durante todo o empréstimo.
        
        Args:
            loan_amount (float): Valor total do empréstimo
            
        Returns:
            float: Total de juros pagos
            
        Raises:
            ValueError: Se o valor do empréstimo for menor ou igual a zero
        """
        if loan_amount <= 0:
            raise ValueError("O valor do empréstimo deve ser maior que zero")
            
        monthly_payment = self.calculate_monthly_payment(loan_amount)
        total_payment = monthly_payment * self.loan_term_months
        
        return total_payment - loan_amount
    
    def calculate_loan_to_value_ratio(self, loan_amount, property_value):
        """
        Calcula a relação entre o valor do empréstimo e o valor do imóvel (LTV).
        
        Args:
            loan_amount (float): Valor total do empréstimo
            property_value (float): Valor do imóvel
            
        Returns:
            float: Relação LTV em percentual
            
        Raises:
            ValueError: Se o valor do empréstimo ou do imóvel for menor ou igual a zero
        """
        if loan_amount <= 0:
            raise ValueError("O valor do empréstimo deve ser maior que zero")
        if property_value <= 0:
            raise ValueError("O valor do imóvel deve ser maior que zero")
            
        return (loan_amount / property_value) * 100


def calculate_future_value(principal, rate, time_years, compounding_frequency="annual"):
    """
    Calcula o valor futuro de um investimento com juros compostos.
    
    Args:
        principal (float): Valor principal (montante inicial)
        rate (float): Taxa de juros anual (em percentual, ex: 5.5 para 5.5%)
        time_years (float): Tempo em anos
        compounding_frequency (str): Frequência de capitalização dos juros:
                                     "annual", "semiannual", "quarterly", "monthly", "daily"
    
    Returns:
        float: Valor futuro do investimento
    
    Raises:
        ValueError: Se o principal for negativo, a taxa for negativa ou o tempo for negativo
        ValueError: Se a frequência de capitalização for inválida
    """
    if principal < 0:
        raise ValueError("O valor principal não pode ser negativo")
    if rate < 0:
        raise ValueError("A taxa de juros não pode ser negativa")
    if time_years < 0:
        raise ValueError("O tempo não pode ser negativo")
    
    # Converter taxa percentual para decimal
    rate_decimal = rate / 100
    
    # Definir o número de períodos de capitalização por ano
    frequency_map = {
        "annual": 1,
        "semiannual": 2,
        "quarterly": 4,
        "monthly": 12,
        "daily": 365
    }
    
    if compounding_frequency not in frequency_map:
        valid_frequencies = ", ".join(frequency_map.keys())
        raise ValueError(f"Frequência de capitalização inválida. Deve ser uma das seguintes: {valid_frequencies}")
    
    n = frequency_map[compounding_frequency]
    
    # Calcular o valor futuro usando a fórmula de juros compostos
    future_value = principal * (1 + rate_decimal / n) ** (n * time_years)
    
    return future_value


def calculate_inflation_adjusted_value(present_value, inflation_rate, years):
    """
    Calcula o valor ajustado pela inflação ao longo do tempo.
    
    Args:
        present_value (float): Valor presente
        inflation_rate (float): Taxa de inflação anual (em percentual, ex: 3.5 para 3.5%)
        years (int): Número de anos
    
    Returns:
        float: Valor ajustado pela inflação
    
    Raises:
        ValueError: Se o valor presente for negativo, a taxa de inflação for negativa ou os anos forem negativos
    """
    if present_value < 0:
        raise ValueError("O valor presente não pode ser negativo")
    if inflation_rate < 0:
        raise ValueError("A taxa de inflação não pode ser negativa")
    if years < 0:
        raise ValueError("O número de anos não pode ser negativo")
    
    # Converter taxa percentual para decimal
    inflation_decimal = inflation_rate / 100
    
    # Calcular o valor ajustado pela inflação
    adjusted_value = present_value / ((1 + inflation_decimal) ** years)
    
    return adjusted_value