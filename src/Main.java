// Felipe Bertacco Haddad 
// RA: 10437372

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class NoArvoreB {
    List<Integer> chaves;        // Lista de chaves armazenadas no nó
    List<NoArvoreB> filhos;      // Ponteiros para os filhos
    boolean ehFolha;             // Indica se o nó é folha

    NoArvoreB(boolean ehFolha) {
        this.ehFolha = ehFolha;
        this.chaves = new ArrayList<>();
        this.filhos = new ArrayList<>();
    }
}

public class ArvoreB {
    private NoArvoreB raiz;
    private final int ordem; // Representa 'd' — mínimo de chaves por nó (exceto raiz)

    public ArvoreB(int ordem) {
        this.ordem = ordem;
        this.raiz = new NoArvoreB(true);
    }

    // Inserção principal
    public void inserir(int chave) {
        NoArvoreB r = raiz;

        // Se a raiz estiver cheia, faz a cisão
        if (r.chaves.size() == 2 * ordem) {
            NoArvoreB novaRaiz = new NoArvoreB(false);
            novaRaiz.filhos.add(r);
            raiz = novaRaiz;
            dividirFilho(novaRaiz, 0);
            inserirNaoCheio(novaRaiz, chave);
        } else {
            inserirNaoCheio(r, chave);
        }
    }

    // Insere uma chave em um nó que ainda não está cheio
    private void inserirNaoCheio(NoArvoreB no, int chave) {
        int i = no.chaves.size() - 1;

        if (no.ehFolha) {
            // Insere a chave mantendo a ordem crescente
            no.chaves.add(chave);
            Collections.sort(no.chaves);
        } else {
            // Encontra o filho apropriado para descer
            while (i >= 0 && chave < no.chaves.get(i)) {
                i--;
            }
            i++;

            // Se o filho estiver cheio, divide antes de descer
            if (no.filhos.get(i).chaves.size() == 2 * ordem) {
                dividirFilho(no, i);
                if (chave > no.chaves.get(i)) {
                    i++;
                }
            }
            inserirNaoCheio(no.filhos.get(i), chave);
        }
    }

    // Realiza a cisão de um filho cheio em dois nós
    private void dividirFilho(NoArvoreB pai, int indiceFilho) {
        NoArvoreB filhoCheio = pai.filhos.get(indiceFilho);
        NoArvoreB novoNo = new NoArvoreB(filhoCheio.ehFolha);

        // Move as chaves da metade superior para o novo nó
        for (int j = 0; j < ordem; j++) {
            novoNo.chaves.add(filhoCheio.chaves.remove(ordem + 1)); // remove da posição d+1 até 2d
        }

        // Se não for folha, também move os filhos correspondentes
        if (!filhoCheio.ehFolha) {
            for (int j = 0; j < ordem + 1; j++) {
                novoNo.filhos.add(filhoCheio.filhos.remove(ordem + 1));
            }
        }

        // A chave mediana sobe para o pai
        int chaveMediana = filhoCheio.chaves.remove(ordem);
        pai.chaves.add(indiceFilho, chaveMediana);
        pai.filhos.add(indiceFilho + 1, novoNo);
    }

    // Impressão simples da árvore por níveis (para debug)
    public void imprimir() {
        imprimirRecursivo(raiz, 0);
    }

    private void imprimirRecursivo(NoArvoreB no, int nivel) {
        System.out.println("Nível " + nivel + ": " + no.chaves);
        for (NoArvoreB filho : no.filhos) {
            imprimirRecursivo(filho, nivel + 1);
        }
    }

    // Teste básico
    public static void main(String[] args) {
        ArvoreB arvore = new ArvoreB(2); // Árvore B de ordem 2

        int[] valores = {20, 40, 10, 30, 15, 50, 60, 70, 80, 90};
        for (int v : valores) {
            arvore.inserir(v);
        }

        arvore.imprimir();
    }
}
