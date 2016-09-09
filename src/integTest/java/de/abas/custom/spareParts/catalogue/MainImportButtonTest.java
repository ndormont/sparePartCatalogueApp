package de.abas.custom.spareParts.catalogue;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import de.abas.custom.spareParts.catalogue.utils.AbstractTest;
import de.abas.erp.common.type.AbasDate;
import de.abas.erp.db.infosystem.custom.ow1.ReplacementCatalogue;
import de.abas.erp.db.schema.custom.replacement.SparePart;
import de.abas.erp.db.schema.vendor.Vendor;
import de.abas.erp.db.schema.vendor.VendorEditor;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;

public class MainImportButtonTest extends AbstractTest {

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yy");
	Vendor vendor;
	SparePart sparePart;

	@Override
	public void cleanup() {
		infosys.abort();
		if (sparePart != null) {
			sparePart.delete();
		}
		if (vendor != null) {
			vendor.delete();
		}
		super.cleanup();
	}

	// TODO: importUpdateTest

	@Test
	public void importCreateTest() {
		infosys.setVendor(vendor);
		infosys.setFile("test.csv");

		infosys.invokeStart();

		assertThat(infosys.table().getRowCount(), is(1));

		final Iterable<ReplacementCatalogue.Row> rows = infosys.table().getEditableRows();
		for (final ReplacementCatalogue.Row row : rows) {
			row.setImport(true);
		}

		infosys.invokeStartimport();

		final List<SparePart> spareParts = ctx.createQuery(SelectionBuilder.create(SparePart.class)
				.add(Conditions.eq(SparePart.META.yvendor, vendor))
				.add(Conditions.eq(SparePart.META.ymatchcode, infosys.table().getRow(1).getMatchcode())).build())
				.execute();

		assertThat(spareParts.size(), is(1));

		sparePart = spareParts.get(0);

		assertThat(sparePart.getYproductno(), is(infosys.table().getRow(1).getProductno()));
		assertThat(sparePart.getYdescr(), is(infosys.table().getRow(1).getDescr()));
		assertThat(sparePart.getYprice(), is(infosys.table().getRow(1).getPrice()));
		assertThat(sparePart.getYtransferred(), is(true));
		assertThat(DATE_FORMATTER.format(sparePart.getYdate().toDate()),
				is(DATE_FORMATTER.format(new AbasDate().toDate())));
		assertThat(DATE_FORMATTER.format(sparePart.getYchanged().toDate()),
				is(DATE_FORMATTER.format(new AbasDate().toDate())));
		assertThat(sparePart.getYsigned(), is(not("")));
	}

	@Override
	public void setup() {
		super.setup();
		createVendor();

	}

	private void createVendor() {
		infosys.abort();
		final VendorEditor vendor = ctx.newObject(VendorEditor.class);
		vendor.setSwd("TEST");
		vendor.commit();
		this.vendor = vendor.objectId();
		infosys = ctx.openInfosystem(ReplacementCatalogue.class);
	}

}