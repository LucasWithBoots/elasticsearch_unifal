import { useMemo, useState } from 'react'
import type { FormEvent } from 'react'
import './App.css'

type SearchResult = {
  title?: string
  url?: string
  abs?: string
}

type RequestStatus = 'idle' | 'loading' | 'success' | 'error'

const PAGE_SIZE = 10
const CURL_COMMAND = `curl -k -u elastic:user123 -X POST "https://localhost:9200/wikipedia/_doc" \\
  -H "Content-Type: application/json" \\
  -d '{
    "title": "Computador",
    "url": "https://example.com/computador",
    "content": "Computador é uma máquina eletrônica capaz de processar dados."
  }'

curl -k -u elastic:user123 -X POST "https://localhost:9200/wikipedia/_refresh"`

function App() {
  const [query, setQuery] = useState('')
  const [searchedQuery, setSearchedQuery] = useState('')
  const [page, setPage] = useState(1)
  const [results, setResults] = useState<SearchResult[]>([])
  const [status, setStatus] = useState<RequestStatus>('idle')
  const [error, setError] = useState('')
  const [copied, setCopied] = useState(false)

  const hasResults = results.length > 0
  const canGoBack = page > 1 && status !== 'loading'
  const canGoNext = results.length === PAGE_SIZE && status !== 'loading'

  const heading = useMemo(() => {
    if (status === 'idle') return 'Pesquise no indice wikipedia'
    if (status === 'loading') return 'Buscando documentos...'
    if (status === 'error') return 'Nao foi possivel buscar'
    if (!hasResults) return `Nenhum resultado para "${searchedQuery}"`
    return `${results.length} resultado${results.length === 1 ? '' : 's'} para "${searchedQuery}"`
  }, [hasResults, results.length, searchedQuery, status])

  async function search(nextPage = 1, nextQuery = query) {
    const normalizedQuery = nextQuery.trim()

    if (!normalizedQuery) {
      setStatus('error')
      setError('Digite um termo para pesquisar.')
      setResults([])
      return
    }

    setStatus('loading')
    setError('')

    try {
      const params = new URLSearchParams({
        query: normalizedQuery,
        page: String(nextPage),
      })
      const response = await fetch(`/v1/search?${params.toString()}`)

      if (!response.ok) {
        throw new Error(`A API respondeu com status ${response.status}.`)
      }

      const data = (await response.json()) as SearchResult[]
      setResults(Array.isArray(data) ? data : [])
      setSearchedQuery(normalizedQuery)
      setPage(nextPage)
      setStatus('success')
    } catch (err) {
      setResults([])
      setStatus('error')
      setError(err instanceof Error ? err.message : 'Erro inesperado na busca.')
    }
  }

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    search(1)
  }

  async function copyCurlCommand() {
    await navigator.clipboard.writeText(CURL_COMMAND)
    setCopied(true)
    window.setTimeout(() => setCopied(false), 1800)
  }

  return (
    <main className="page">
      <section className="hero">
        <div className="brand">
          <span className="brand-mark">ES</span>
          <div>
            <p className="eyebrow">Elasticsearch Search API</p>
            <h1>Busca simples para seus documentos</h1>
          </div>
        </div>

        <form className="search-box" onSubmit={handleSubmit}>
          <label htmlFor="query">Termo de busca</label>
          <div className="search-row">
            <input
              id="query"
              value={query}
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Ex.: computador, java, elasticsearch"
              type="search"
            />
            <button disabled={status === 'loading'} type="submit">
              {status === 'loading' ? 'Buscando...' : 'Buscar'}
            </button>
          </div>
        </form>
      </section>

      <section className="results-panel" aria-live="polite">
        <div className="results-header">
          <div>
            <p className="eyebrow">Resultados</p>
            <h2>{heading}</h2>
          </div>

          <div className="pagination" aria-label="Paginacao">
            <button
              aria-label="Pagina anterior"
              disabled={!canGoBack}
              onClick={() => search(page - 1, searchedQuery || query)}
              type="button"
            >
              ‹
            </button>
            <span>Pagina {page}</span>
            <button
              aria-label="Proxima pagina"
              disabled={!canGoNext}
              onClick={() => search(page + 1, searchedQuery || query)}
              type="button"
            >
              ›
            </button>
          </div>
        </div>

        {status === 'idle' && (
          <div className="state-card">
            <strong>Pronto para consultar</strong>
            <p>Digite uma palavra-chave para buscar no indice `wikipedia`.</p>
          </div>
        )}

        {status === 'loading' && (
          <div className="state-card">
            <span className="loader" />
            <p>Consultando o backend Spring Boot...</p>
          </div>
        )}

        {status === 'error' && (
          <div className="state-card state-card-error">
            <strong>Erro na consulta</strong>
            <p>{error}</p>
          </div>
        )}

        {status === 'success' && !hasResults && (
          <div className="state-card">
            <strong>Nada encontrado</strong>
            <p>Confira se existem documentos no Elasticsearch com esse termo.</p>
          </div>
        )}

        {hasResults && (
          <div className="result-list">
            {results.map((result, index) => (
              <article className="result-card" key={`${result.url ?? result.title ?? 'result'}-${index}`}>
                <div>
                  <h3>{result.title || 'Sem titulo'}</h3>
                  <p>{result.abs || 'Documento sem resumo disponivel.'}</p>
                </div>

                {result.url && (
                  <a className="result-link" href={result.url} rel="noreferrer" target="_blank">
                    Abrir URL
                  </a>
                )}
              </article>
            ))}
          </div>
        )}
      </section>

      <section className="curl-panel" aria-labelledby="curl-title">
        <div className="curl-header">
          <div>
            <p className="eyebrow">Adicionar documento</p>
            <h2 id="curl-title">Exemplo de comando curl</h2>
            <p>Execute no terminal para inserir um registro e atualizar o indice.</p>
          </div>

          <button className="copy-button" onClick={copyCurlCommand} type="button">
            {copied ? 'Copiado' : 'Copiar comando'}
          </button>
        </div>

        <pre>
          <code>{CURL_COMMAND}</code>
        </pre>
      </section>
    </main>
  )
}

export default App
