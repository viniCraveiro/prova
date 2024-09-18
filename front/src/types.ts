export interface Periferico {
  nome: string;
}

export interface Computador {
  id: string;
  nome: string | undefined;
  cor: string | undefined;
  dataFabricacao: number | undefined;
  perifericos: Periferico[];
}
