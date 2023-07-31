# INTRODUÇÃO
Uma expressão regular (ER) que represente determinada linguagem pode ser convertida em um autômato finito determinístico (AFD) que uma sentença qualquer pertencente a mesma linguagem.

O objetivo deste trabalho é implementar um programa determine se uma dada sentença pertence a linguagem representada por uma dada ER a partir de sua conversão
desta última para um AFD equivalente, portanto devem ser implementados:
    
  - um programa converta uma ER qualquer para um AFD equivalente; e
  - um simulador de AFD.

# OBJETIVO

A implementação deve receber como entrada um arquivo com várias sentenças e um arquivo contendo a descrição de uma ER qualquer.
Em seguida, deve-se gerar como saída outro arquivo contendo a
descrição do AFD equivalente e, além disso, simular o AFD equivalente (para cada sentença do outro arquivo de entrada) para se determinar se as sentenças pertencem ou não à linguagem da ER.

Além disso, deve-se utilizar como formato para os arquivos de entrada e saída o mesmo padrão adotado pelo simulador JFLAP versão 7.0 que pode ser encontrado em http://www.jflap.org/ (favor prestar muita
atenção na versão a ser utilizada)

# ENTREGA

  - Código fonte
  - Executável
  - Relatório em PDF escrito em LaTeX
  - Código fonte do relatório
  - Exemplos de entradas e as respectivas saídas
