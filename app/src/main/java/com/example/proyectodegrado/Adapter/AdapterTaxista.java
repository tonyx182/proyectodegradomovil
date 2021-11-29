package com.example.proyectodegrado.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodegrado.Carreras;
import com.example.proyectodegrado.R;

import java.util.List;

public class AdapterTaxista extends RecyclerView.Adapter<AdapterTaxista.ViewHolder>{
    private List<Carreras> lista;
    ItemClickListener itemClickListener;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textFecha;
        private final TextView textCosto;
        private final TextView textTitle;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textFecha = (TextView) view.findViewById(R.id.textFecha);
            textCosto = (TextView) view.findViewById(R.id.textCosto);
            textTitle = (TextView) view.findViewById(R.id.textTitle);
        }

        public TextView getTextFecha() {
            return textFecha;
        }
        public TextView getTextCosto() {
            return textCosto;
        }
        public TextView getTextTilte() {
            return textTitle;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public AdapterTaxista(List<Carreras> listaC, ItemClickListener i) {
        lista = listaC;
        itemClickListener = i;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public AdapterTaxista.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_taxista, viewGroup, false);

        return new AdapterTaxista.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterTaxista.ViewHolder viewHolder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Carreras c = lista.get(position);
        viewHolder.getTextFecha().setText("Fecha del Viaje: " + c.fechaServicio);
        viewHolder.getTextCosto().setText("Costo del Viaje: " + String.valueOf(c.costo) + " Bs.");
        viewHolder.getTextTilte().setText(c.descripcion);
        viewHolder.textTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClickItem(c);
            }
        });
    }


    // Replace the contents of a view (invoked by the layout manager)


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() { return lista.size(); }
}
