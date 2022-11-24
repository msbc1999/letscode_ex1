package me.mateus.letscode.ex1.loja.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import me.mateus.letscode.ex1.api.Generate;
import me.mateus.letscode.ex1.api.Get;
import me.mateus.letscode.ex1.api.Linker;
import me.mateus.letscode.ex1.loja.catalogo.*;
import me.mateus.letscode.ex1.loja.produto.*;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static me.mateus.letscode.ex1.loja.regras.Restricao.MAIOR_18_ANOS;
import static me.mateus.letscode.ex1.utils.Utils.set;
import static me.mateus.letscode.ex1.utils.Utils.unmodifiable;

public final class FornecedorService {

    private final Map<Mercadoria, Generate<Produto<?>>> mercadorias = ofEntries(//
            criarMercadoria(AlbumMusicaProduto::new, new AlbumMusica("Top Brasil 2022", 45_00L, set("Joaozinho", "Mariazinha"), set("Pop", "Eletrônica"), set("Ouro"), set())),//
            criarMercadoria(AlbumMusicaProduto::new, new AlbumMusica("Top Brasil 2021", 19_50L, set("Pedrinho", "Aninha"), set("Pop"), set("Ouro"), set())),//
            criarMercadoria(BrinquedoProduto::new, new Brinquedo("Carrinho de controle remoto", 25_90L, "Ação", set())),//
            criarMercadoria(BrinquedoProduto::new, new Brinquedo("Cartas coloridas", 12_78L, "Estratégia", set())),//
            criarMercadoria(FilmeProduto::new, new Filme("Parque da alegria", 14_99L, "Estudio Amizade", set("Joaozinho"), set("Ação", "Aventura"), set("Longa Produções"), set())),//
            criarMercadoria(FilmeProduto::new, new Filme("Fritadeira assassina", 18_25L, "Infarto Colaborações", set("Pedrinho"), set("Terror", "Suspense"), set("Impressionante Fx"), set(MAIOR_18_ANOS))),//
            criarMercadoria(JogoProduto::new, new Jogo("Fazendinha da vovó - Deluxe Edition", 46_89L, "Dinamica Soluções", set("Simulação"), "Brinca e Diverte Games", set())),//
            criarMercadoria(JogoProduto::new, new Jogo("Sniper medieval II", 89_65L, "Quase Infinito", set("RPG", "Ação"), "Futuro Games", set(MAIOR_18_ANOS))),//
            criarMercadoria(LivroProduto::new, new Livro("Java lindo, bonito e cheiroso - Curso Avançado", 32_45L, set("Programação"), "Prof. Teclaudio", "Conhecimento e Aprendizado", set(MAIOR_18_ANOS))),//
            criarMercadoria(LivroProduto::new, new Livro("A arte de nomear - Variáveis e Constantes", 56_12L, set("Programação", "Tecnologia"), "Josésese", "Ler e Aprender", set()))//
    );
    private <T1 extends Mercadoria, T2 extends Produto<?>> Entry<Mercadoria, Generate<Produto<?>>> criarMercadoria(Function<T1, T2> construtor, T1 mercadoria) {return entry(mercadoria, () -> construtor.apply(mercadoria));}
    public Get<Mercadoria> linkMercadoriasDisponiveis() {return () -> unmodifiable(this.mercadorias.keySet());}
    public Linker<Mercadoria, Produto<?>> linkFornecerProduto() {return (mercadoria, post) -> () -> post.post(this.mercadorias.get(mercadoria).produce());}

}