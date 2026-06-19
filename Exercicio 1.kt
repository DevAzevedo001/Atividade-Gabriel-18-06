open class Produto(
    val nome: String,
    private var preco: Double,
    var quantidadeEstoque: Int
) {

    fun getPreco(): Double {
        return preco
    }

    fun setPreco(valor: Double) {
        if (valor >= 0) {
            preco = valor
        } else {
            println("Preço inválido.")
        }
    }

    fun aplicarDesconto(percentual: Double) {
        if (percentual < 0 || percentual > 100) {
            println("Desconto inválido.")
            return
        }

        val novoPreco = preco - (preco * percentual / 100)
        setPreco(novoPreco)
    }

    open fun resumo(): String {
        return "Produto: $nome | Preço: R$ ${"%.2f".format(preco)} | Estoque: $quantidadeEstoque"
    }
}

class ProdutoPerecivel(
    nome: String,
    preco: Double,
    quantidadeEstoque: Int,
    val dataValidade: String
) : Produto(nome, preco, quantidadeEstoque) {

    fun estaVencido(dataHoje: String): Boolean {
        return dataValidade < dataHoje
    }

    override fun resumo(): String {
        return "${super.resumo()} | Validade: $dataValidade"
    }
}

abstract class FuncionarioBase(
    val nome: String,
    val salarioBase: Double
) {
    abstract fun calcularSalario(): Double
}

class Vendedor(
    nome: String,
    salarioBase: Double,
    var totalVendas: Double
) : FuncionarioBase(nome, salarioBase) {

    override fun calcularSalario(): Double {
        return salarioBase + (totalVendas * 0.05)
    }
}

class Gerente(
    nome: String,
    salarioBase: Double,
    val bonusFixo: Double
) : FuncionarioBase(nome, salarioBase) {

    override fun calcularSalario(): Double {
        return salarioBase + bonusFixo
    }
}

fun finalizarVenda(
    carrinho: List<Produto>,
    vendedor: Vendedor
) {
    var totalVenda = 0.0

    for (produto in carrinho) {

        if (produto is ProdutoPerecivel) {
            if (produto.estaVencido("2026/06/18")) {
                println("AVISO: ${produto.nome} está vencido.")
            }
        }

        println("${produto.nome} - R$ ${"%.2f".format(produto.getPreco())}")

        totalVenda += produto.getPreco()
        produto.quantidadeEstoque--
    }

    vendedor.totalVendas += totalVenda

    println("--------------------")
    println("Total da venda: R$ ${"%.2f".format(totalVenda)}")
    println("Salário atualizado: R$ ${"%.2f".format(vendedor.calcularSalario())}")
}

fun main() {

    println("=== PRODUTOS ===")

    val arroz = Produto("Arroz", 8.50, 100)
    val feijao = Produto("Feijão", 7.00, 50)
    val macarrao = Produto("Macarrão", 4.50, 200)

    println(arroz.resumo())
    println(feijao.resumo())
    println(macarrao.resumo())

    arroz.aplicarDesconto(10.0)
    feijao.aplicarDesconto(20.0)

    println("\nApós descontos:")
    println(arroz.resumo())
    println(feijao.resumo())
    println(macarrao.resumo())

    println("\n=== PRODUTO PERECÍVEL ===")

    val leite = ProdutoPerecivel(
        "Leite",
        6.50,
        20,
        "2025/01/10"
    )

    println(leite.resumo())
    println("Leite vencido? ${leite.estaVencido("2026/06/18")}")

    println("\n=== FUNCIONÁRIOS ===")

    val vendedor = Vendedor(
        "João",
        3000.0,
        5000.0
    )

    val gerente = Gerente(
        "Maria",
        5000.0,
        1000.0
    )

    val funcionarios = listOf<FuncionarioBase>(
        vendedor,
        gerente
    )

    var totalFolha = 0.0

    for (f in funcionarios) {
        val salario = f.calcularSalario()

        println("${f.nome} -> R$ ${"%.2f".format(salario)}")

        totalFolha += salario
    }

    println("--------------------")
    println("Total da folha: R$ ${"%.2f".format(totalFolha)}")

    println("\n=== VENDA ===")

    val carrinho = listOf(
        arroz,
        feijao,
        leite
    )

    finalizarVenda(carrinho, vendedor)
}